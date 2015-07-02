package com.kinwae.popularmovies.net;

import android.net.Uri;
import android.util.Log;

//import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;


/**
 * Created by Kembene on 6/24/2015.
 */
public class NetworkRequest {

    public static final String API_KEY = "54d33209a6e7e146aad6b7ce16875a32";
    public static final String BASE_URL = "http://api.themoviedb.org/3/";

    public static final String DISCOVERY_PATH = "discover/movie";


    private static final String LOG_TAG = NetworkRequest.class.getName();
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();

    private static final String QUERY_PARAM_API_KEY = "api_key";
    private static final String QUERY_PARAM_SORT = "sort_by";

    static {
        //todo remove in production
        //HTTP_CLIENT.networkInterceptors().add(new StethoInterceptor());
    }

    public void executeAsync(NetworkResponseCallback callback){
        executeAsync(null, callback);
    }

    public void executeAsync(String sorting, NetworkResponseCallback callback){
        Uri uri = RequestBuilder.buildUri(sorting);
        initiateRequest(uri, callback);
    }

    public NetworkResponse execute(String sorting){
        Uri uri = RequestBuilder.buildUri(sorting);
        return execute(uri);
    }

    public NetworkResponse execute(Uri builtUri){
        return initiateRequest(builtUri, null);
    }

    /**
     * Using this method to test we i've gat no connection
     * @param builtUri
     * @param userCallback
     *
     * @return
     */
    protected NetworkResponse initiateRequest_(Uri builtUri, final NetworkResponseCallback userCallback) {
        NetworkResponse response = new NetworkResponse(builtUri, testMovieString, 200 );
        if(userCallback != null){
            userCallback.onResponseReceived(response);
            return null;
        }
        else {
            return response;
        }
    }

    protected NetworkResponse initiateRequest(Uri builtUri, final NetworkResponseCallback userCallback) {
        String url = builtUri.toString();
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url).build();


        Log.d(LOG_TAG, "Intiating network call to "+url);
        if(userCallback != null){
            // its requested that the call be made asynchronously
            Callback returnedCallback = new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.e(LOG_TAG, "Network call failed with: "+e.getMessage());
                    e.printStackTrace();
                    userCallback.onFailure(e);
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    NetworkResponse tNetworkResponse = null;
                    try{
                        tNetworkResponse = processResponse(response);
                        userCallback.onResponseReceived(tNetworkResponse);
                    }
                    catch (Exception ex){
                        userCallback.onFailure(ex);
                    }
                    finally {
                        try{
                            if(response != null)
                                response.body().close();
                        }
                        catch (Exception ex){
                            Log.e(LOG_TAG, "Exception while closing the response request response "+ ex.getMessage());
                            ex.printStackTrace();
                        }
                    }
                }
            };

            HTTP_CLIENT.newCall(request).enqueue(returnedCallback);
            return null;
        }
        else{
            NetworkResponse networkResponse = null;
            Response response = null;
            try {
                response = HTTP_CLIENT.newCall(request).execute();
                networkResponse = processResponse(response);
            } catch (Exception e) {
                e.printStackTrace();
                if(networkResponse == null){
                    networkResponse = new NetworkResponse(e);
                }
                else{
                    networkResponse.setCallException(e);
                }

            }
            finally {
                try{
                    if(response != null)
                        response.body().close();
                }
                catch (Exception ex){
                    Log.e(LOG_TAG, "Exception while closing the response request response "+ ex.getMessage());
                    ex.printStackTrace();
                }
            }
            return networkResponse;
        }
    }


    private NetworkResponse processResponse(Response response)throws IOException{
        NetworkResponse networkResponse = null;
        Uri requestUri = Uri.parse(response.request().uri().toString());
        if(response.isSuccessful()){
            String responseString = response.body().string();
            Log.v(LOG_TAG, "Fetching of request succeeded with content: "+responseString);
            networkResponse = new NetworkResponse(requestUri, responseString, response.code());
        }
        else{
            Log.v(LOG_TAG, "Unable to fetch data: " + response.code() + " - " + response.message());
            networkResponse = new NetworkResponse(requestUri, null, response.code());
        }

        return networkResponse;
    }



    public static class RequestBuilder{
        private static NetworkRequest SHARED_INSTANCE;
        /**
         * returns the URI that can be used to discover movies sorted by the specified movieSorting
         * @return
         */
        public static NetworkRequest buildPopularMovieRequest(){
            return new NetworkRequest();
        }

        public static NetworkRequest sharedInstance(){
            if(SHARED_INSTANCE == null){
                SHARED_INSTANCE = new NetworkRequest();
            }
            return SHARED_INSTANCE;
        }

        protected static Uri buildUri(String sorting){
            return Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM_API_KEY, API_KEY)
                    .appendQueryParameter(QUERY_PARAM_SORT, sorting)
                    .build();
        }


        public static Uri.Builder discoveryUri(String sorting){
            return Uri.parse(BASE_URL + DISCOVERY_PATH).buildUpon()
                    .appendQueryParameter(QUERY_PARAM_API_KEY, API_KEY)
                    .appendQueryParameter(QUERY_PARAM_SORT, sorting);
        }
    }

    public static final String testMovieString = "{\"page\":1,\"results\":[{\"adult\":false,\"backdrop_path\":\"/dkMD5qlogeRMiEixC4YNPUvax2T.jpg\",\"genre_ids\":[28,12,878,53],\"id\":135397,\"original_language\":\"en\",\"original_title\":\"Jurassic World\",\"overview\":\"Twenty-two years after the events of Jurassic Park, Isla Nublar now features a fully functioning dinosaur theme park, Jurassic World, as originally envisioned by John Hammond.\",\"release_date\":\"2015-06-12\",\"poster_path\":\"/uXZYawqUsChGSj54wcuBtEdUJbh.jp\",\"popularity\":75.180696,\"title\":\"Jurassic World\",\"video\":false,\"vote_average\":7.0,\"vote_count\":620},{\"adult\":false,\"backdrop_path\":\"/tbhdm8UJAb4ViCTsulYFL3lxMCd.jpg\",\"genre_ids\":[53,28,12],\"id\":76341,\"original_language\":\"en\",\"original_title\":\"Mad Max: Fury Road\",\"overview\":\"An apocalyptic story set in the furthest reaches of our planet, in a stark desert landscape where humanity is broken, and most everyone is crazed fighting for the necessities of life. Within this world exist two rebels on the run who just might be able to restore order. There's Max, a man of action and a man of few words, who seeks peace of mind following the loss of his wife and child in the aftermath of the chaos. And Furiosa, a woman of action and a woman who believes her path to survival may be achieved if she can make it across the desert back to her childhood homeland.\",\"release_date\":\"2015-05-15\",\"poster_path\":\"/kqjL17yufvn9OVLyXYpvtyrFfak.jpg\",\"popularity\":56.655018,\"title\":\"Mad Max: Fury Road\",\"video\":false,\"vote_average\":7.9,\"vote_count\":877},{\"adult\":false,\"backdrop_path\":\"/xjjO3JIdneMBTsS282JffiPqfHW.jpg\",\"genre_ids\":[10749,14,10751,18],\"id\":150689,\"original_language\":\"en\",\"original_title\":\"Cinderella\",\"overview\":\"When her father unexpectedly passes away, young Ella finds herself at the mercy of her cruel stepmother and her daughters. Never one to give up hope, Ella's fortunes begin to change after meeting a dashing stranger in the woods.\",\"release_date\":\"2015-03-13\",\"poster_path\":\"/2i0JH5WqYFqki7WDhUW56Sg0obh.jpg\",\"popularity\":30.488162,\"title\":\"Cinderella\",\"video\":false,\"vote_average\":7.2,\"vote_count\":299},{\"adult\":false,\"backdrop_path\":\"/cUfGqafAVQkatQ7N4y08RNV3bgu.jpg\",\"genre_ids\":[28,18,53],\"id\":254128,\"original_language\":\"en\",\"original_title\":\"San Andreas\",\"overview\":\"In the aftermath of a massive earthquake in California, a rescue-chopper pilot makes a dangerous journey across the state in order to rescue his estranged daughter.\",\"release_date\":\"2015-05-29\",\"poster_path\":\"/6iQ4CMtYorKFfAmXEpAQZMnA0Qe.jpg\",\"popularity\":26.879069,\"title\":\"San Andreas\",\"video\":false,\"vote_average\":6.3,\"vote_count\":270},{\"adult\":false,\"backdrop_path\":\"/2BXd0t9JdVqCp9sKf6kzMkr7QjB.jpg\",\"genre_ids\":[12,10751,16,28,35],\"id\":177572,\"original_language\":\"en\",\"original_title\":\"Big Hero 6\",\"overview\":\"The special bond that develops between plus-sized inflatable robot Baymax, and prodigy Hiro Hamada, who team up with a group of friends to form a band of high-tech heroes.\",\"release_date\":\"2014-11-07\",\"poster_path\":\"/3zQvuSAUdC3mrx9vnSEpkFX0968.jpg\",\"popularity\":22.165293,\"title\":\"Big Hero 6\",\"video\":false,\"vote_average\":8.0,\"vote_count\":1505},{\"adult\":false,\"backdrop_path\":\"/y5lG7TBpeOMG0jxAaTK0ghZSzBJ.jpg\",\"genre_ids\":[28,878,53],\"id\":198184,\"original_language\":\"en\",\"original_title\":\"Chappie\",\"overview\":\"Every child comes into the world full of promise, and none more so than Chappie: he is gifted, special, a prodigy. Like any child, Chappie will come under the influence of his surroundings—some good, some bad—and he will rely on his heart and soul to find his way in the world and become his own man. But there's one thing that makes Chappie different from any one else: he is a robot.\",\"release_date\":\"2015-03-06\",\"poster_path\":\"/saF3HtAduvrP9ytXDxSnQJP3oqx.jpg\",\"popularity\":18.290192,\"title\":\"Chappie\",\"video\":false,\"vote_average\":6.7,\"vote_count\":518},{\"adult\":false,\"backdrop_path\":\"/xu9zaAevzQ5nnrsXN6JcahLnG4i.jpg\",\"genre_ids\":[18,878],\"id\":157336,\"original_language\":\"en\",\"original_title\":\"Interstellar\",\"overview\":\"Interstellar chronicles the adventures of a group of explorers who make use of a newly discovered wormhole to surpass the limitations on human space travel and conquer the vast distances involved in an interstellar voyage.\",\"release_date\":\"2014-11-05\",\"poster_path\":\"/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg\",\"popularity\":18.021829,\"title\":\"Interstellar\",\"video\":false,\"vote_average\":8.4,\"vote_count\":2522},{\"adult\":false,\"backdrop_path\":\"/9eKd1DDDAbrDNXR2he7ZJEu7UkI.jpg\",\"genre_ids\":[28,12,35,80],\"id\":207703,\"original_language\":\"en\",\"original_title\":\"Kingsman: The Secret Service\",\"overview\":\"Kingsman: The Secret Service tells the story of a super-secret spy organization that recruits an unrefined but promising street kid into the agency's ultra-competitive training program just as a global threat emerges from a twisted tech genius.\",\"release_date\":\"2015-02-13\",\"poster_path\":\"/oAISjx6DvR2yUn9dxj00vP8OcJJ.jpg\",\"popularity\":16.566409,\"title\":\"Kingsman: The Secret Service\",\"video\":false,\"vote_average\":7.7,\"vote_count\":1101},{\"adult\":false,\"backdrop_path\":\"/4liSXBZZdURI0c1Id1zLJo6Z3Gu.jpg\",\"genre_ids\":[878,14,28,12],\"id\":76757,\"original_language\":\"en\",\"original_title\":\"Jupiter Ascending\",\"overview\":\"In a universe where human genetic material is the most precious commodity, an impoverished young Earth woman becomes the key to strategic maneuvers and internal strife within a powerful dynasty…\",\"release_date\":\"2015-02-27\",\"poster_path\":\"/aMEsvTUklw0uZ3gk3Q6lAj6302a.jpg\",\"popularity\":16.80306,\"title\":\"Jupiter Ascending\",\"video\":false,\"vote_average\":5.4,\"vote_count\":714},{\"adult\":false,\"backdrop_path\":\"/qhH3GyIfAnGv1pjdV3mw03qAilg.jpg\",\"genre_ids\":[12,14],\"id\":122917,\"original_language\":\"en\",\"original_title\":\"The Hobbit: The Battle of the Five Armies\",\"overview\":\"Mere seconds after the events of \\\"Desolation\\\", Bilbo and Company continue to claim a mountain of treasure that was guarded long ago: But with Gandalf the Grey also facing some formidable foes of his own, the Hobbit is outmatched when the brutal army of orcs led by Azog the Defiler returns. But with other armies such as the elves and the men of Lake-Town, which are unsure to be trusted, are put to the ultimate test when Smaug's wrath, Azog's sheer strength, and Sauron's force of complete ends attack. All in all, the trusted armies have two choices: unite or die. But even worse, Bilbo gets put on a knife edge and finds himself fighting with Hobbit warfare with all of his might for his dwarf-friends, as the hope for Middle-Earth is all put in Bilbo's hands. The one \\\"precious\\\" thing to end it all.\",\"release_date\":\"2014-12-17\",\"poster_path\":\"/qrFwjJ5nvFnpBCmXLI4YoeHJNBH.jpg\",\"popularity\":16.406608,\"title\":\"The Hobbit: The Battle of the Five Armies\",\"video\":false,\"vote_average\":7.2,\"vote_count\":1489},{\"adult\":false,\"backdrop_path\":\"/szytSpLAyBh3ULei3x663mAv5ZT.jpg\",\"genre_ids\":[35,16,10751],\"id\":150540,\"original_language\":\"en\",\"original_title\":\"Inside Out\",\"overview\":\"Growing up can be a bumpy road, and it's no exception for Riley, who is uprooted from her Midwest life when her father starts a new job in San Francisco. Like all of us, Riley is guided by her emotions - Joy, Fear, Anger, Disgust and Sadness. The emotions live in Headquarters, the control center inside Riley's mind, where they help advise her through everyday life. As Riley and her emotions struggle to adjust to a new life in San Francisco, turmoil ensues in Headquarters. Although Joy, Riley's main and most important emotion, tries to keep things positive, the emotions conflict on how best to navigate a new city, house and school.\",\"release_date\":\"2015-06-19\",\"poster_path\":\"/rDycdoAXtBb7hoWlBpZqbwk2F44.jpg\",\"popularity\":16.343076,\"title\":\"Inside Out\",\"video\":false,\"vote_average\":8.6,\"vote_count\":111},{\"adult\":false,\"backdrop_path\":\"/kJre98tnbNXbk5L5altHkQWGwD3.jpg\",\"genre_ids\":[28,12,878,9648],\"id\":158852,\"original_language\":\"en\",\"original_title\":\"Tomorrowland\",\"overview\":\"Bound by a shared destiny, a bright, optimistic teen bursting with scientific curiosity and a former boy-genius inventor jaded by disillusionment embark on a danger-filled mission to unearth the secrets of an enigmatic place somewhere in time and space that exists in their collective memory as \\\"Tomorrowland.\\\"\",\"release_date\":\"2015-05-22\",\"poster_path\":\"/69Cz9VNQZy39fUE2g0Ggth6SBTM.jpg\",\"popularity\":15.845895,\"title\":\"Tomorrowland\",\"video\":false,\"vote_average\":6.6,\"vote_count\":263},{\"adult\":false,\"backdrop_path\":\"/bHarw8xrmQeqf3t8HpuMY7zoK4x.jpg\",\"genre_ids\":[878,14,12],\"id\":118340,\"original_language\":\"en\",\"original_title\":\"Guardians of the Galaxy\",\"overview\":\"Light years from Earth, 26 years after being abducted, Peter Quill finds himself the prime target of a manhunt after discovering an orb wanted by Ronan the Accuser.\",\"release_date\":\"2014-08-01\",\"poster_path\":\"/9gm3lL8JMTTmc3W4BmNMCuRLdL8.jpg\",\"popularity\":15.558007,\"title\":\"Guardians of the Galaxy\",\"video\":false,\"vote_average\":8.2,\"vote_count\":2730},{\"adult\":false,\"backdrop_path\":\"/rFtsE7Lhlc2jRWF7SRAU0fvrveQ.jpg\",\"genre_ids\":[12,878,28],\"id\":99861,\"original_language\":\"en\",\"original_title\":\"Avengers: Age of Ultron\",\"overview\":\"When Tony Stark tries to jumpstart a dormant peacekeeping program, things go awry and Earth’s Mightiest Heroes are put to the ultimate test as the fate of the planet hangs in the balance. As the villainous Ultron emerges, it is up to The Avengers to stop him from enacting his terrible plans, and soon uneasy alliances and unexpected action pave the way for an epic and unique global adventure.\",\"release_date\":\"2015-05-01\",\"poster_path\":\"/t90Y3G8UGQp0f0DrP60wRu9gfrH.jpg\",\"popularity\":15.007541,\"title\":\"Avengers: Age of Ultron\",\"video\":false,\"vote_average\":7.8,\"vote_count\":1298},{\"adult\":false,\"backdrop_path\":\"/fii9tPZTpy75qOCJBulWOb0ifGp.jpg\",\"genre_ids\":[36,18,53,10752],\"id\":205596,\"original_language\":\"en\",\"original_title\":\"The Imitation Game\",\"overview\":\"Based on the real life story of legendary cryptanalyst Alan Turing, the film portrays the nail-biting race against time by Turing and his brilliant team of code-breakers at Britain's top-secret Government Code and Cypher School at Bletchley Park, during the darkest days of World War II.\",\"release_date\":\"2014-11-14\",\"poster_path\":\"/noUp0XOqIcmgefRnRZa1nhtRvWO.jpg\",\"popularity\":14.959202,\"title\":\"The Imitation Game\",\"video\":false,\"vote_average\":8.1,\"vote_count\":1259},{\"adult\":false,\"backdrop_path\":\"/razvUuLkF7CX4XsLyj02ksC0ayy.jpg\",\"genre_ids\":[80,28,53],\"id\":260346,\"original_language\":\"en\",\"original_title\":\"Taken 3\",\"overview\":\"Ex-government operative Bryan Mills finds his life is shattered when he's falsely accused of a murder that hits close to home. As he's pursued by a savvy police inspector, Mills employs his particular set of skills to track the real killer and exact his unique brand of justice.\",\"release_date\":\"2015-01-09\",\"poster_path\":\"/c2SSjUVYawDUnQ92bmTqsZsPEiB.jpg\",\"popularity\":14.787165,\"title\":\"Taken 3\",\"video\":false,\"vote_average\":6.2,\"vote_count\":715},{\"adult\":false,\"backdrop_path\":\"/o4I5sHdjzs29hBWzHtS2MKD3JsM.jpg\",\"genre_ids\":[878,28,53,12],\"id\":87101,\"original_language\":\"en\",\"original_title\":\"Terminator Genisys\",\"overview\":\"The year is 2029. John Connor, leader of the resistance continues the war against the machines. At the Los Angeles offensive, John's fears of the unknown future begin to emerge when TECOM spies reveal a new plot by SkyNet that will attack him from both fronts; past and future, and will ultimately change warfare forever.\",\"release_date\":\"2015-07-01\",\"poster_path\":\"/qOoFD4HD9a2EEUymdzBQN9XF1UJ.jpg\",\"popularity\":14.378929,\"title\":\"Terminator Genisys\",\"video\":false,\"vote_average\":7.0,\"vote_count\":40},{\"adult\":false,\"backdrop_path\":\"/anItUS64TeGKPv6MJ99DMv7o0Z0.jpg\",\"genre_ids\":[35,10402],\"id\":254470,\"original_language\":\"en\",\"original_title\":\"Pitch Perfect 2\",\"overview\":\"The Bellas are back, and they are better than ever. After being humiliated in front of none other than the President of the United States of America, the Bellas are taken out of the Aca-Circuit. In order to clear their name, and regain their status, the Bellas take on a seemingly impossible task: winning an international competition no American team has ever won. In order to accomplish this monumental task, they need to strengthen the bonds of friendship and sisterhood and blow away the competition with their amazing aca-magic! With all new friends and old rivals tagging along for the trip, the Bellas can hopefully accomplish their dreams.\",\"release_date\":\"2015-05-15\",\"poster_path\":\"/qSjruLiFB4uqRtz2xheQPxG8uaB.jpg\",\"popularity\":12.939307,\"title\":\"Pitch Perfect 2\",\"video\":false,\"vote_average\":7.3,\"vote_count\":211},{\"adult\":false,\"backdrop_path\":\"/fUn5I5f4069vwGFEEvA3HXt9xPP.jpg\",\"genre_ids\":[878,12,53],\"id\":131631,\"original_language\":\"en\",\"original_title\":\"The Hunger Games: Mockingjay - Part 1\",\"overview\":\"Katniss Everdeen reluctantly becomes the symbol of a mass rebellion against the autocratic Capitol.\",\"release_date\":\"2014-11-20\",\"poster_path\":\"/cWERd8rgbw7bCMZlwP207HUXxym.jpg\",\"popularity\":12.905696,\"title\":\"The Hunger Games: Mockingjay - Part 1\",\"video\":false,\"vote_average\":7.0,\"vote_count\":1397},{\"adult\":false,\"backdrop_path\":\"/tGxBYYSKSmBUpjSHDpRelsbNeBy.jpg\",\"genre_ids\":[80,53,28],\"id\":334074,\"original_language\":\"en\",\"original_title\":\"Survivor\",\"overview\":\"A Foreign Service Officer in London tries to prevent a terrorist attack set to hit New York, but is forced to go on the run when she is framed for crimes she did not commit.\",\"release_date\":\"2015-05-29\",\"poster_path\":\"/hHuM1sYhGkWu3wTLotnNeiSJrpz.jpg\",\"popularity\":12.243829,\"title\":\"Survivor\",\"video\":false,\"vote_average\":5.4,\"vote_count\":49}],\"total_pages\":11577,\"total_results\":231522}";

}
