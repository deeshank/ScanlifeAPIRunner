import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zgao on 8/3/2015.
 */
public class SignaturGenerator_getCodeList {
    public static void main(String args[]) {
        // signature for create code request
        //
        // // MODIFIED WITH OUR APIKEY/SECRETKEY
        // //
        String apiKey = "53e396a8-107d-11e6-b656-08002760fc47";
        String secretKey = "secret";
        String request = null;
        String params = "";
        String signature = null;
        String encoded_sig = null;
        try {
            // Createcode service url for your reference
            String url = "http://api.scanlife.com/api/code/getcodelist";
            // parameters append after the url. we are generating signature
            // using this parameters and secretyKey
            String queryString = "codename=test&codetype=web&apikey=" + apiKey;
            StringBuffer buffer = new StringBuffer();
            String secretyKey = secretKey;
            // String secretyKey = "scanbuy";

            // sortedmap using for sort the param name and storing also.
            SortedMap<String, String> sortedParamMap = new TreeMap<String, String>();
            sortedParamMap.put("userid", "5");


            // // added url parameter
            // //
            //sortedParamMap.put("url","http://eco-return.com/Endicia/api/shoes4u");

            // // added timestamp
            // //
            Date today = new Date();
            SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
                    "Y-M-d'T'H:m:s'Z'");
            DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
            String timestamp = DATE_FORMAT.format(today);
            System.out.println("timestamp=" + timestamp);

            sortedParamMap.put("timestamp", timestamp);

            sortedParamMap.put("apikey", apiKey);

            Iterator<Map.Entry<String, String>> iter = sortedParamMap
                    .entrySet().iterator();
            // create the canonical string using the URLEncoder and utf-8
            while (iter.hasNext()) {
                Map.Entry<String, String> kvpair = iter.next();
                buffer.append(URLEncoder
                        .encode(kvpair.getKey(), "UTF-8")
                        .replace("+", "%20").replace("*", "%2A")
                        .replace("%7E", "~"));
                buffer.append("=");
                buffer.append(URLEncoder
                        .encode(kvpair.getValue(), "UTF-8")
                        .replace("+", "%20").replace("*", "%2A")
                        .replace("%7E", "~"));
                if (iter.hasNext()) {
                    buffer.append("&");
                }
            }

            byte[] data = buffer.toString().getBytes("UTF-8");
            // using HmacSHA256 process
            Mac mac = Mac.getInstance("HmacSHA256");
            byte[] secretyKeyBytes = secretyKey.getBytes("UTF-8");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretyKeyBytes,
                    "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] rawHmac = mac.doFinal(data);
            Base64 encoder = new Base64();
            signature = new String(encoder.encode(rawHmac));
            // finally we are generate the signature
            System.out.println("signature=" + signature);

            // after create signature createcode request like following
            System.out.println("http://localhost/api/code/getcodelist?"
                    + buffer.toString() + "&signature="
                    + URLEncoder.encode(signature, "UTF-8"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
