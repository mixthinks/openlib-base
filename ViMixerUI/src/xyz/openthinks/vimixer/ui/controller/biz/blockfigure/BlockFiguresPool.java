package xyz.openthinks.vimixer.ui.controller.biz.blockfigure;

import java.util.concurrent.LinkedBlockingQueue;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import xyz.openthinks.vimixer.ui.model.ViFile;

/**
 * {@link BlocksView} cache pool and request queue
 * @author minjdai
 *
 */
public final class BlockFiguresPool {
	private final static ObservableMap<ViFile, BlocksView> caches = FXCollections
			.observableHashMap();
	private static BlockOverViewFigure currentFigure;

	private final static LinkedBlockingQueue<BlockOverViewFigure> concurrentLinkedQueue = new LinkedBlockingQueue<BlockOverViewFigure>();
	
	/**
	 * try to get {@link BlocksView} from cache firstly, if not exist, create a new one to reference the {@link ViFile}
	 * @param vifile 
	 * @return BlocksView
	 */
	public final static BlocksView get(ViFile vifile) {
		if (caches.containsKey(vifile)) {
			return caches.get(vifile);
		} else {
			caches.put(vifile, new BlocksView());
			return caches.get(vifile);
		}
	}

	/**
	 * 
	 * @return BlockOverViewFigure
	 */
	public static final BlockOverViewFigure currentFigure(){
		return currentFigure;
	}
	
	public final static void push(BlockOverViewFigure overViewFigure) {
		currentFigure = overViewFigure;
		concurrentLinkedQueue.add(overViewFigure);
	}

	public final static void active() {
		new BlockFiguresPoolThread().start();
	}

	private final static class BlockFiguresPoolThread extends Thread {
		@Override
		public void run() {
			BlockOverViewFigure picked = null;
			do {
				try {
					picked = BlockFiguresPool.concurrentLinkedQueue.take();
					if (BlockFiguresPool.currentFigure == picked)
						picked.render();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} while (true);
		}
	}
}