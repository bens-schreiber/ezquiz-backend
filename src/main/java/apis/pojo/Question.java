package apis.pojo;

public class Question {

    private String quizowner, question, answer, directions, quizname, subject, type, options;


    public String getQuizowner() {
        return quizowner;
    }

    public String getQuizname() {
        return quizname;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getType() {
        return type;
    }

    public String getSubject() {
        return subject;
    }

    public String getOptions() {
        return options;
    }

    public String getDirections() {
        return directions;
    }

    public void setQuizowner(String quizowner) {
        this.quizowner = quizowner;
    }

    public void setQuizname(String quizname) {
        this.quizname = quizname;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }
    }
