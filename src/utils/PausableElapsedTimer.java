package utils;

import utilities.ElapsedTimer;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class PausableElapsedTimer extends ElapsedTimer {

	long acc = 0;
	boolean isPaused = false;

	public void pause(){
		if(!isPaused)
			acc += super.elapsed();
		isPaused = true;
	}

	public void start(){
		if(isPaused)
			this.reset();
		isPaused = false;
	}

	@Override
	public long elapsed() {
		if(isPaused)
			return acc;
		return acc+super.elapsed();
	}

	public String prettyElapsed(){
		long duration = elapsed();
		long milliseconds = duration % 1000;
		long seconds = ((duration - milliseconds) / 1000) % 60;
		long minutes = ((duration/1000 - seconds) / 60) % 60;
		long hours = ((duration/1000/60 - minutes) / 60) % 24;
		long days = (duration/1000/60/60 - hours) / 24;

		StringBuilder b = new StringBuilder();
		if(days>0)
			b.append(days+" d ");
		if(hours>0)
			b.append(hours+" h ");
		if(minutes>0)
			b.append(minutes+" m ");
		if(seconds>0)
			b.append(seconds+" s ");
		if(milliseconds>0)
			b.append(milliseconds+" ms ");

		return b.toString();
	}

	private String details() {
		return "PausableElapsedTimer{" +
				"acc=" + acc +
				", isPaused=" + isPaused +
				", extra="+ super.elapsed()+
				'}';
	}

	public static void main(String[] args) {
		int runCount = 10;
		long pauseDuration = 1;
		PausableElapsedTimer timer = new PausableElapsedTimer();
		try {
			for(int i=0; i<runCount; i++){
				TimeUnit.SECONDS.sleep(pauseDuration);
				timer.pause();
				TimeUnit.SECONDS.sleep(pauseDuration);
				timer.start();
				System.out.println(timer.details());
				System.out.println("elapsed millis[timer]: "+timer.elapsed()+" {loop: "+i+"}");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("elapsed millis[timer]: "+timer.elapsed());
		System.out.println("elapsed millis[timer pretty]: "+timer.prettyElapsed());
		System.out.println("elapsed millis[theoretical]: "+pauseDuration*1000*runCount);
	}
}
