import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailSentenceExtractor {

    public static void main(String[] args) {
        String filePath = "D:\\task\\document.txt"; 
        List<String> firstWords = extractFirstWordsWithEmails(filePath);
        
        // Вивід результату
        if (firstWords.isEmpty()) {
            System.out.println("Не знайдено речень з електронними адресами.");
        } else {
            for (String word : firstWords) {
                System.out.println(word);
            }
        }
    }

    private static List<String> extractFirstWordsWithEmails(String filePath) {
        List<String> firstWords = new ArrayList<>();
        StringBuilder content = new StringBuilder();

      
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(" ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Вміст файлу: " + content.toString());

        String emailRegex = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
        Pattern emailPattern = Pattern.compile(emailRegex);
      
        String[] sentences = content.toString().split("(?<=[.!?])\\s*");

        for (String sentence : sentences) {
            Matcher emailMatcher = emailPattern.matcher(sentence);
            if (emailMatcher.find()) {
                String firstWord = sentence.split("\\s+")[0];
                firstWords.add(firstWord);
            }
        }

        return firstWords;
    }
}
