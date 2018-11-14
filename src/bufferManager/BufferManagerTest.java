package bufferManager;

import static org.junit.Assert.*;

import org.junit.Test;

import baseDeDonnee.PageId;

public class BufferManagerTest {

	@Test
	public void test() {
		BufferManager bm = BufferManager.getInstance();
		PageId pid1 = new PageId(1, 1);
		PageId pid2 = new PageId(2, 2);
		PageId pid3 = new PageId(3, 3);
		PageId pid4 = new PageId(4, 4);
		PageId pid5 = new PageId(5, 5);
		bm.get(pid1);
		bm.get(pid2);
		bm.get(pid2);
		bm.get(pid2);
		assertEquals(bm.getFrame1().getPageId(), pid1);
		assertEquals(bm.getFrame2().getPageId(), pid2);
		assertEquals(bm.getFrame1().getPinCount(), 1);
		assertEquals(bm.getFrame2().getPinCount(), 3);
		bm.free(pid2, true);
		assertEquals(bm.getFrame2().getPinCount(), 2);
		assertEquals(bm.getFrame2().getDirty(), true);
		bm.get(pid3);
		assertEquals(bm.getFrame1().getPageId(), pid2);
		assertEquals(bm.getFrame2().getPageId(), pid3);
		bm.free(pid2, true);
		bm.free(pid2, true);
		bm.free(pid3, false);
		bm.get(pid4);
		assertEquals(bm.getFrame1().getPageId(), pid2);
		assertEquals(bm.getFrame2().getPageId(), pid4);
		bm.free(pid4, true);
		bm.get(pid5);
		assertEquals(bm.getFrame1().getPageId(), pid4);
		assertEquals(bm.getFrame2().getPageId(), pid5);
		
		
	}

}
