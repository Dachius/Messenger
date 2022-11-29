package Spam;

/* *
 *  How to use spamFilter program.
 * 
 *  There is a variable called previousMessage which is implemented to check what messages are sent.
 *  If the message is the same as the last one then it returns a boolean value of false.
 *  If the message is different then it returns a true value.
 *  Using this we can demand whether to send the message or not.
 * 
 *  It takes two variables called previousMessage and currentMessage.
 *  The String variable previousMessage has the last message sent if there is one.
 *  The currentMessage is also a String variable that is being sent.
 * */

public class spamFilter {
    public boolean noSpam(String previousMessage, String currentMessage){
        boolean spamBool = true;

        //This should return True and send the message.
        if(previousMessage != currentMessage){
            spamBool = true;
        }

        //This should return False and deny sending the spam message.
        if(previousMessage == currentMessage){
            spamBool = false;
        }
        return spamBool;
    }
}
