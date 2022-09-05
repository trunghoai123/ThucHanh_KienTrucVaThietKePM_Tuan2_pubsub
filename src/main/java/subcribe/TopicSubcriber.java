package subcribe;

import java.util.Properties;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import org.apache.log4j.BasicConfigurator;
public class TopicSubcriber {
public static void main(String[] args) throws Exception{
//thiáº¿t láº­p mÃ´i trÆ°á»�ng cho JMS
BasicConfigurator.configure();
//thiáº¿t láº­p mÃ´i trÆ°á»�ng cho JJNDI
Properties settings=new Properties();
settings.setProperty(Context.INITIAL_CONTEXT_FACTORY,
"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
settings.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
//táº¡o context
Context ctx=new InitialContext(settings);
//lookup JMS connection factory
Object obj=ctx.lookup("TopicConnectionFactory");
ConnectionFactory factory=(ConnectionFactory)obj;
//táº¡o connection
Connection con=factory.createConnection("admin","admin");
//ná»‘i Ä‘áº¿n MOM
con.start();
//táº¡o session
Session session=con.createSession(
/*transaction*/false,
/*ACK*/Session.CLIENT_ACKNOWLEDGE
);
//táº¡o consumer
Destination destination=(Destination) ctx.lookup("dynamicTopics/thanthidet");
MessageConsumer receiver = session.createConsumer(destination);
//receiver.receive();//blocked method
//Cho receiver láº¯ng nghe trÃªn queue, chá»«ng cÃ³ message thÃ¬ notify
receiver.setMessageListener(new MessageListener() {
//@Override
//cÃ³ message Ä‘áº¿n queue, phÆ°Æ¡ng thá»©c nÃ y Ä‘Æ°á»£c thá»±c thi
public void onMessage(Message msg) {//msg lÃ  message nháº­n Ä‘Æ°á»£c
try {
if(msg instanceof TextMessage){
TextMessage tm=(TextMessage)msg;
String txt=tm.getText();
System.out.println("XML= "+txt);
msg.acknowledge();//gá»­i tÃ­n hiá»‡u ack
}
} catch (Exception e) {
e.printStackTrace();
}
}
});
}
}