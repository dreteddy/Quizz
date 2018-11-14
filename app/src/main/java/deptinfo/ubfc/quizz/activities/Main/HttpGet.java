package deptinfo.ubfc.quizz.activities.Main;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import deptinfo.ubfc.quizz.helpers.DatabaseHelper;
import deptinfo.ubfc.quizz.models.Answer;
import deptinfo.ubfc.quizz.models.Question;
import deptinfo.ubfc.quizz.models.Quiz;

public class HttpGet {
    private static final String LOG_TAG = "HttpClientGET";
    public ArrayList<Quiz> quizs;
    public static HttpPage tacheHttpPage;
    private Context mcontext;
    private DatabaseHelper dbHelper = null;

    public HttpGet(DatabaseHelper dbHelper){
        this.quizs = new ArrayList<Quiz>();

        //Création d'une instance de ma classe DatabaseHelper
        this.dbHelper = dbHelper;

        this.tacheHttpPage = new HttpPage();

        this.dbHelper.closeDB();

    }

    private void getPage(String adresse) {
        BufferedReader bufferedReader = null;
        HttpURLConnection urlConnection = null;
        try {

            URL url = new URL(adresse);

            urlConnection = (HttpURLConnection) url.openConnection();
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = urlConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(inputStream);
                doc.getDocumentElement().normalize();

                Node channelElement = doc.getElementsByTagName("Quizzs").item(0);
                addQuiz(channelElement.getChildNodes().item(0), quizs);

            } else {
                Log.i(LOG_TAG, "Response : " + responseCode);
            }

        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, e.getMessage());
                }
            }
            if (urlConnection != null) urlConnection.disconnect();
        }
    }

    public class HttpPage extends AsyncTask<Void, Void, List> {

        @Override
        protected List doInBackground(Void... params) {
            getPage("https://dept-info.univ-fcomte.fr/joomla/images/CR0700/Quizzs.xml");
            return quizs;
        }

        @Override
        public void onPostExecute(List result) { ;
            Log.i(LOG_TAG, "List Executed");
        }
    }

    /*
     Get Quizs from nodes
     */
    public void addQuiz(Node quizNode, ArrayList<Quiz> quizs){

        if (!(quizNode instanceof Text)){
            // Get Quiz text from xml
            String quizType = quizNode.getAttributes().item(0).getNodeValue();

            //Create new Quiz
            Quiz quiz = new Quiz();
            quiz.setQuizType(quizType);
            quizs.add(quiz);

            //Insert to DB
            long quizID = dbHelper.createQuiz(quiz);
            quiz.setId(quizID);

            Node questionNode = ((Element) quizNode).getElementsByTagName("Question").item(0);

            //Add Questions to quiz
            addQuestion(questionNode, quiz);
        }

        Node sibling = quizNode.getNextSibling();
        if(sibling != null)
            addQuiz(sibling, quizs);
    }

    /*
     Get Questions from nodes
     */
    public void addQuestion(Node questionNode, Quiz quiz){

        if (!(questionNode instanceof Text)){

            // Get Question text from xml
            String quest = questionNode.getChildNodes().item(0).getNodeValue();

            //Create new Question
            Question question = new Question();
            question.setQuestion(quest);

            //Add Question to Quiz questions
            quiz.addQuestion(question);

            //Add Correct answer number
            Node correctAnswer = ((Element) questionNode).getElementsByTagName("Reponse").item(0);
            String correctAnswerValue = correctAnswer.getAttributes().item(0).getNodeValue();
            question.setCorrect_answer(Integer.parseInt(correctAnswerValue));

            //Insert to DB
            long questionID = dbHelper.createQuestion(question , quiz);
            question.setId(questionID);

            //Add Answers to Question answers
            Node answerNode = ((Element) questionNode).getElementsByTagName("Proposition").item(0);
            addAnswers(answerNode, question);

        }

        Node sibling = questionNode.getNextSibling();
        if(sibling != null)
            addQuestion(sibling, quiz);
    }

    /*
    /// Get Answers from nodes
     */
    public void addAnswers(Node answerNode, Question question){

        if (!(answerNode instanceof Text)){

            // Get answer text from xml
            String ans = answerNode.getChildNodes().item(0).getNodeValue();

            //Create new answer
            Answer answer = new Answer();
            answer.setAnswer(ans);

            //Add answer to Question answers
            question.addAnswer(answer);

            //Insert to DB
            long answerID = dbHelper.createAnswer(answer, question);
            answer.setId(answerID);
        }

        Node sibling = answerNode.getNextSibling();
        if(sibling != null)
            addAnswers(sibling, question);
    }
}
