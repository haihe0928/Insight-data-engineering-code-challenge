package venmo;
/**use minHeap to maintain the upper half of payments which have large degree, in current time window
 * use maxHeap to maintain the lower half of payments which have small degree, in current time window
 * the size of minHeap is equal to or 1 larger than the size of minHeap
 * if total size is even, current median degree is the average of the top elements of minHeap and maxHeap
 * if total size is odd, current median degree is the degree of the top elemwnt of minHeap
*/

public class MedianDegree {
	private MinPriorityQueue minHeap = new MinPriorityQueue();
	private MaxPriorityQueue maxHeap = new MaxPriorityQueue();

	public MedianDegree() {
		
	}

	public void increment(String name) {
		if (!minHeap.containsName(name) && !maxHeap.containsName(name)) {
			VerticeDegree newVertice = new VerticeDegree(name, 1);
			if (maxHeap.size() == minHeap.size()) {
				if (minHeap.size() == 0 || newVertice.getDegree() >= minHeap.peek().getDegree()) {
					minHeap.offer(newVertice);
				} else {
					maxHeap.offer(newVertice);
					minHeap.offer(maxHeap.poll());
				}
			} else {
				if (newVertice.getDegree() < minHeap.peek().getDegree()) {
					maxHeap.offer(newVertice);
				} else {
					minHeap.offer(newVertice);
					maxHeap.offer(minHeap.poll());
				}
			}
		} else if (maxHeap.containsName(name)) {
			maxHeap.updateDegree(name, 1);
			if (maxHeap.peek().getDegree() > minHeap.peek().getDegree()) {
				if (minHeap.size() > maxHeap.size()) {
					minHeap.offer(maxHeap.poll());
					maxHeap.offer(minHeap.poll());
				} else {
					minHeap.offer(maxHeap.poll());
				}
			}

		} else {
			minHeap.updateDegree(name, 1);
		}
	}

	public void decrement(String name) {
		if (maxHeap.containsName(name)) {
			maxHeap.updateDegree(name, -1);
			if (minHeap.size() - maxHeap.size() >= 2) {
				maxHeap.offer(minHeap.poll());
			}
		} else {
			minHeap.updateDegree(name, -1);
			if (minHeap.peek().getDegree() >= maxHeap.peek().getDegree()) {
				if (minHeap.size() < maxHeap.size()) {
					minHeap.offer(maxHeap.poll());
				}
			} else {
				if (minHeap.size() > maxHeap.size()) {
					maxHeap.offer(minHeap.poll());
				} else {
					VerticeDegree vertice = maxHeap.poll();
					maxHeap.offer(minHeap.poll());
					minHeap.offer(vertice);
				}
			}
		}
	}
//public API used to return median degree
	public Double median() {
		if (minHeap.size() == 0 && maxHeap.size() == 0) {
			return null;
		} else if (minHeap.size() > maxHeap.size()) {
			return (double) minHeap.peek().getDegree() * 1.00;
		}
		return ((maxHeap.peek().getDegree() + minHeap.peek().getDegree()) / 2.00);
	}

}
