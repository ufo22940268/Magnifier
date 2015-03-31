package chroya.demo.magnifier;

/**
 * Created by ccheng on 3/31/15.
 */
public class WordHelper {
    public static String extractWordForPosition(String demoString, int offsetForPosition) {
        String[] split = demoString.split(" ");
        int cnt = 0;
        for (int i = 0; i < split.length; i++) {
            if (cnt <= offsetForPosition) {
                cnt += split[i].length();
                cnt += 1;
            }

            if (cnt > offsetForPosition) {
                return split[i];
            }
        }

        return null;
    }
}
