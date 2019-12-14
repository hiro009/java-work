package hiro.work.util.cycle;

import java.util.concurrent.Callable;

public class CycleProcessFactory {

	public static final CycleRunnable generateCycleRunnable(long interval, CycleProcess<Void> cycleProcess) throws InterruptedException {
		return new CycleRunnable(cycleProcess, interval);
	}

	public static final <T> CycleCallable<T> generateCycleCallable(long interval, CycleProcess<T> cycleProcess) throws Exception {
		return new CycleCallable<T>(cycleProcess, interval);
	}

	public static interface CycleProcess<T> {
		T call();

		boolean isContinue();
	}

	public static class CycleRunnable implements Runnable {
		private final CycleProcess<Void> process;
		private final long interval;

		private CycleRunnable(CycleProcess<Void> process, long interval) {
			this.process = process;
			this.interval = interval;
		}

		@Override
		public void run() {
			try {
				while (process.isContinue()) {
					process.call();
					Thread.sleep(interval);
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	public static class CycleCallable<T> implements Callable<T> {
		
		private final CycleProcess<T> process;
		private final long interval;

		private CycleCallable(CycleProcess<T> process, long interval){
			this.process = process;
			this.interval = interval;
		}
		
		@Override
		public T call() throws Exception {
			T res = null;
			while (process.isContinue()) {
				res = process.call();
				Thread.sleep(interval);
			}
			return res;		
		}
	}

}
