package android.com.galatube.GEYoutubeEvents;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by deepak on 28/11/17.
 */

public class GEDateUtils {
    /*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

        public static final List<Long> times = Arrays.asList(
                TimeUnit.DAYS.toMillis(365),
                TimeUnit.DAYS.toMillis(30),
                TimeUnit.DAYS.toMillis(1),
                TimeUnit.HOURS.toMillis(1),
                TimeUnit.MINUTES.toMillis(1),
                TimeUnit.SECONDS.toMillis(1) );
        public static final List<String> timesString = Arrays.asList("year","month","day","hour","minute","second");

    public static String getTimeAgo(long time) {

        StringBuffer res = new StringBuffer();
        for(int i=0;i< GEDateUtils.times.size(); i++) {
            Long current = GEDateUtils.times.get(i);
            long temp = time/current;
            if(temp>0) {
                res.append(temp).append(" ").append( GEDateUtils.timesString.get(i) ).append(temp != 1 ? "s" : "").append(" ago");
                break;
            }
        }
        if("".equals(res.toString()))
            return "0 seconds ago";
        else
            return res.toString();
    }
}
