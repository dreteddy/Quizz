package deptinfo.ubfc.quizz;

import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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

import deptinfo.ubfc.quizz.model.Answer;
import deptinfo.ubfc.quizz.model.Question;
import deptinfo.ubfc.quizz.model.Quiz;

public class HttpGet {
    private static final String LOG_TAG = "HttpClientGET";
    public List<String> returnList;
    public static HttpPage tacheHttpPage;


    HttpGet(){
        Log.d("OUI", "HttpClientGet");
        this.returnList = new ArrayList<>();
        tacheHttpPage = new HttpPage();
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


                //NodeList nodeList;

                Node channelElement = doc.getElementsByTagName("Quizzs").item(0);
                ArrayList<Quiz> quizs = new ArrayList<Quiz>();
                addQuiz(channelElement, quizs);
                //doSomething(channelElement);
                /*nodeList = ((Element) channelElement).getElementsByTagName("Question");

                StringBuffer stringBuffer = new StringBuffer();
                if (nodeList.getLength() != 0) {
                    stringBuffer.append(nodeList.item(0).getChildNodes().item(0).getNodeValue());

                    for (int i = 0; i< nodeList.getLength(); i++){
                        this.returnList.add(nodeList.item(i).getFirstChild().getNodeValue());
                    }
                    Log.d(LOG_TAG, "Node added to List");

                } else {
                    //
                }*/



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
            return returnList;
        }


        @Override
        public void onPostExecute(List result) { ;
            Log.i(LOG_TAG, "List Executed");
        }
    }

    public static void doSomething(Node node) {
        // do something with the current node instead of System.out
        //System.out.println(node.getNodeName());

        ArrayList<Quiz> quizs = new ArrayList<Quiz>();
        Log.i("NodeName", node.getNodeName());

        NamedNodeMap attributes = node.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node nodeA = attributes.item(i);
            Log.i("AttVal", nodeA.getNodeValue());
        }

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node currentNode = nodeList.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE ) {
                //calls this method for all the children which is Element
                doSomething(currentNode);
            }
        }
    }

    public static void addAnswers(Node node, Question question){
        NodeList nodeList =((Element) node).getElementsByTagName("Proposition");;
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node currentNode = nodeList.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE ) {
                Answer answer =  new Answer(nodeList.item(i).getFirstChild().getNodeValue());
                question.getAnswers().add(answer);
            }
        }
    }

    public static void addQuestion(Node node, Quiz quiz){
        NodeList nodeList =((Element) node).getElementsByTagName("Question");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node currentNode = nodeList.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE ) {
                Question question =  new Question();
                question.setQuestion(nodeList.item(i).getFirstChild().getNodeValue());
                quiz.addQuestion(question);
                //addAnswers(currentNode, question);

                //Add correct answer value

            }
        }
    }

    public static void addQuiz(Node node, ArrayList<Quiz> quizs){
        NodeList nodeList = node.getChildNodes();

        //for (int i = 0; i< nodeList.getLength(); i++){
            Log.i("NodeName", nodeList.item(1).getNodeName());
            Log.i("NodeName", nodeList.item(1).getAttributes().item(0).getNodeValue());
            String quizType = nodeList.item(1).getAttributes().item(0).getNodeValue();
            Quiz quiz = new Quiz();
            quiz.setQuizType(quizType);

    }
}
