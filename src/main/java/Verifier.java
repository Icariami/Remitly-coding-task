import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.util.Objects;

/**
 * JSON input data verifier class.
 */
public class Verifier {

    /**
     * Verifies the JSON input data from the specified file.
     * Checks is input data format is AWS::IAM::Role Policy.
     * Throws a runtime exception in case the input file does not meet the requirements
     * @param fileName name of the JSON file
     * @return false if an input JSON Resource field contains a single asterisk, true in any other case.
     */
    public static boolean verify(String fileName) {
        if (fileName == null)
            throw new NullPointerException("File name is null");
        if (!fileName.endsWith(".json"))
            throw new IllegalArgumentException("Provided file is not of .json type");

        try (InputStream stream = Verifier.class.getClassLoader().getResourceAsStream(fileName)) {
            if (stream == null)
                throw new NullPointerException("Error occurred while loading the file stream");
            try (Reader reader = new InputStreamReader(stream)) {

                JSONParser parser = new JSONParser();
                JSONObject mainObject = (JSONObject) parser.parse(reader);

                Object policyName =  mainObject.get("PolicyName");
                if (!(policyName instanceof String))
                    throw new IllegalArgumentException("Incorrect data format - PolicyName is not a String.");

                JSONObject policyDocument = Objects.requireNonNull((JSONObject) mainObject.get("PolicyDocument"), "Incorrect data format - Policy Document is null");
                JSONArray statementArray = Objects.requireNonNull((JSONArray) policyDocument.get("Statement"), " Incorrect data format - Statement is null");
                if (statementArray.isEmpty())
                    throw new IllegalArgumentException("Incorrect data format - Statement array is empty");
                JSONObject statement = (JSONObject) statementArray.getFirst();
                String resource = Objects.requireNonNull((String) statement.get("Resource"), " Incorrect data format - Resource is null");

                return !resource.equals("*");

            } catch (IOException | ParseException | RuntimeException e) {
                throw new RuntimeException(e.getMessage());
            }
        } catch (IOException | NullPointerException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
