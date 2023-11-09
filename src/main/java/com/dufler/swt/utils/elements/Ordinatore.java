package com.dufler.swt.utils.elements;

import java.util.Date;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

/**
 * Questa classe è un template per realizzare l'ordinamento su tabelle
 * 
 * @author Damiano Bellucci - damiano.bellucci@gmail.com
 *
 */
public abstract class Ordinatore<T> extends ViewerComparator {

	public enum Direction {
		DESCENDING(1),
		ASCENDING(0);
		
		private final int value;
		
		private Direction(int direction) {
			value = direction;
		}
		
		public int getValue() {
			return value;
		}
	}

	protected int propertyIndex;
	protected int direction;

	public Ordinatore() {
		propertyIndex = 0;
		direction = Direction.DESCENDING.getValue();
	}

	public void setColumn(int column) {
		if (column == this.propertyIndex) {
			// Same column as last sort; toggle the direction
			direction = 1 - direction ;
		} else {
			// New column; do an ascending sort
			this.propertyIndex = column;
			direction = Direction.DESCENDING.getValue();
		}
	}
	
	public int getDirection() {
		return direction;
	}
	
	public void setDirection(Direction direction) {
		this.direction = direction.getValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		T t1 = (T) e1;
        T t2 = (T) e2;
        int rc = compare(t1, t2, propertyIndex);
        // If descending order, flip the direction
        if (direction == Direction.DESCENDING.getValue()) {
                rc = -rc;
        }
        return rc;
	}
	
	protected abstract int compare(T t1, T t2, int property);
	
	/**
	 * Ordina le due stringhe passate come argomento.
	 * Vengono effettuati controlli sulla presenza di valori <code>null</code>.
	 * @param s1 la prima stringa.
	 * @param s2 la seconda stringa.
	 * @return l'ordinamento.
	 */
	protected int compareString(String t1, String t2) {
		if (t1 == null) t1 = "";
		if (t2 == null) t2 = "";
		int compare = t1.compareTo(t2);
		return compare;
	}
	
	/**
	 * Ordina i due booleani passati come argomento.
	 * Vengono effettuati controlli sulla presenza di valori <code>null</code>.
	 * @param i1 il primo intero.
	 * @param i2 il secondo intero.
	 * @return l'ordinamento.
	 */
	protected int compareBoolean(Boolean t1, Boolean t2) {
		if (t1 == null) t1 = false;
		if (t2 == null) t2 = false;
		int compare = t1.compareTo(t2);
		return compare;
	}
	
	/**
	 * Ordina i due interi passati come argomento.
	 * Vengono effettuati controlli sulla presenza di valori <code>null</code>.
	 * @param i1 il primo intero.
	 * @param i2 il secondo intero.
	 * @return l'ordinamento.
	 */
	protected int compareInteger(Integer t1, Integer t2) {
		if (t1 == null) t1 = 0;
		if (t2 == null) t2 = 0;
		int compare = t1.compareTo(t2);
		return compare;
	}
	
	/**
	 * Ordina i due numeri passati come argomento.
	 * Vengono effettuati controlli sulla presenza di valori <code>null</code>.
	 * @param i1 il primo numero.
	 * @param i2 il secondo numero.
	 * @return l'ordinamento.
	 */
	protected int compareDouble(Double t1, Double t2) {
		if (t1 == null) t1 = 0.0;
		if (t2 == null) t2 = 0.0;
		int compare = t1.compareTo(t2);
		return compare;
	}
	
	/**
	 * Ordina le due date passate come argomento.
	 * Vengono effettuati controlli sulla presenza di valori <code>null</code>.
	 * @param d1 la prima data.
	 * @param d2 la seconda data.
	 * @return l'ordinamento.
	 */
	protected int compareDate(Date t1, Date t2) {
		if (t1 == null) t1 = new Date();
		if (t2 == null) t2 = new Date();
		int compare = t1.compareTo(t2);
		return compare;
	}
	
}
