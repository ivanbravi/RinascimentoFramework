package utils.math;

public class Median {

	public static double compute(double[] array) {
		int len = array.length;
		int mid = len / 2;
		quickSort(array, 0, len - 1);
		if (len % 2 == 0) {
			return .5 * (array[mid - 1] + array[mid]);
		} else {
			return array[mid];
		}

	}

	private static void quickSort(double[] array, int low, int high) {
		int i = low, j = high;
		double pivot = array[low + (high - low) / 2];

		double exchange;
		while (i <= j) {
			while (array[i] < pivot) {
				i++;
			}
			while (array[j] > pivot) {
				j--;
			}
			if (i <= j) {
				exchange = array[i];
				array[i] = array[j];
				array[j] = exchange;
				i++;
				j--;
			}
		}
		if (low < j)
			quickSort(array, low, j);
		if (i < high)
			quickSort(array, i, high);
	}
}