package <package name>;
  
@Component  
@Service (interface="java.lang.Runnable")  
@Property (name="scheduler.expression" value="0 0/10 * * * ?", type="String")  
public class MyScheduledTask implements Runnable {  
	public void run() {  
	//place events to run here.  
	}  
}  