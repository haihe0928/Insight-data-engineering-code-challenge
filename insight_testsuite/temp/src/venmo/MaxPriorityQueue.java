package venmo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class MaxPriorityQueue {
	private List<VerticeDegree> list = new ArrayList<>();
	private Map<String, Integer> map = new HashMap<>();
	private int size;
	
	public MaxPriorityQueue() {
		size = 0;
	}
	
	public int size() {
		return size;
	}
	
	public boolean inEmpty() {
		return size == 0;
	}
	
	public VerticeDegree peek() {
		if (size == 0) {
			return null;
		}
		return list.get(0);
	}
	
	public VerticeDegree poll() {
		if (size == 0) {
			throw new NoSuchElementException("Heap is empty!");
		}
		VerticeDegree result = list.get(0);
		
		if (size == 1) {
			map.remove(result.getName());
			list.remove(size - 1);
			size--;
			return result;
		}
		
		list.set(0, list.get(size - 1));
		map.remove(result.getName());
		map.put(list.get(size - 1).getName(), 0);
		list.remove(size - 1);
		size--;
		percolateDown(0);
		return result;
	}
	
	public void offer(VerticeDegree vertice) {
		list.add(vertice);
		map.put(vertice.getName(), size);
		size++;
		percolateUp(size - 1);
	}
	
	
	public boolean containsName(String name) {
		return map.containsKey(name);
	}
	
	public void updateDegree(String name, int deltaDegree) {
		int oldPosition = map.get(name);
		if (deltaDegree == -1 && list.get(oldPosition).getDegree() == 1) {
			if (oldPosition == size - 1) {
				map.remove(list.get(oldPosition).getName());
				list.remove(size - 1);
				size--;
			} else {
				list.set(oldPosition, list.get(size - 1));
				map.remove(name);
				map.put(list.get(size - 1).getName(), oldPosition);
				list.remove(size - 1);
				size--;
				percolateDown(oldPosition);
			}
		} else if (deltaDegree == -1) {
			int oldDegree = list.get(oldPosition).getDegree();
			list.get(oldPosition).setDegree(oldDegree + deltaDegree);
			percolateDown(oldPosition);
		} else {
			int oldDegree = list.get(oldPosition).getDegree();
			list.get(oldPosition).setDegree(oldDegree + deltaDegree);
			percolateUp(oldPosition);
		}
	}

	
	private void percolateUp(int index) {
		while (index > 0) {
			int parentIndex = (index - 1) / 2;
			if (list.get(parentIndex).compareTo(list.get(index)) < 0) {
				swap(list, parentIndex, index);
			} else {
				break;
			}
			index = parentIndex;
		}
	}
	
	private void percolateDown(int index) {
		while (size >= 2 && index <= (size - 2) / 2) {
			int leftChildIndex = index * 2 + 1;
			int rightChildIndex = index * 2 + 2;
			int swapCandidate = leftChildIndex;
			if (rightChildIndex <= size - 1 && list.get(leftChildIndex).compareTo(list.get(rightChildIndex)) <= 0) {
				swapCandidate = rightChildIndex;
			}
			if (list.get(index).compareTo(list.get(swapCandidate)) < 0) {
				swap(list, index, swapCandidate);
			} else {
				break;
			}
			index = swapCandidate;
		}
	}
	
	private void swap(List<VerticeDegree> list, int i, int j) {
		map.put(list.get(i).getName(), j);
		map.put(list.get(j).getName(), i);
		VerticeDegree vertice = list.get(i);
		list.set(i, list.get(j));
		list.set(j, vertice);
	}
	
}
