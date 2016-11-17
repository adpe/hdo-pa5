package ch.ffhs.hdo.client.ui.base;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Observable;

import javax.swing.event.SwingPropertyChangeSupport;

public abstract class Model extends Observable {

	private final PropertyChangeSupport listeners;

	protected Model() {
	        listeners = new PropertyChangeSupport(this);
	    }

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		listeners.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		listeners.removePropertyChangeListener(listener);
	}

	public Iterator<PropertyChangeListener> listenerIterator() {
		return Arrays.asList(listeners.getPropertyChangeListeners()).iterator();
	}

	/**
	 * Notifies all listeners of a property change. null -> null is not a
	 * property change!
	 * 
	 * @param propertyName
	 *            The name of the property that changed
	 * @param oldValue
	 *            The value of the property before the change
	 * @param newValue
	 *            The new value of the property
	 */
	protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		if (oldValue != null || newValue != null) {
			listeners.firePropertyChange(propertyName, oldValue, newValue);
		}
	}

	/**
	 * Notifies all listeners of a property change. null -> null is not a
	 * property change!
	 * 
	 * @param event
	 *            The PropertyChangeEvent
	 */
	protected void firePropertyChange(PropertyChangeEvent event) {
		if (event.getOldValue() != null || event.getNewValue() != null) {
			listeners.firePropertyChange(event);
		}
	}

	/**
	 * Notifies all listeners of an item that was added to a collection.
	 * <p>
	 * 
	 * The property name reported by this method will have <code>#added</code>
	 * as suffix to indicate a change to the list content, not the list
	 * reference itself. The reported <code>newValue</code> will be the object
	 * that was added.
	 * 
	 * @param propertyName
	 *            The name of the collection property that was changed
	 * @param addedElement
	 *            The element that was added to the collection
	 */
	protected void fireItemAddedToCollection(String propertyName, Object addedElement) {
		listeners.firePropertyChange(propertyName + "#added", null, addedElement);
	}

	/**
	 * Notifies all listeners of an item that was removed from a collection.
	 * <p>
	 * 
	 * The property name reported by this method will have <code>#removed</code>
	 * as suffix to indicate a change to the list content, not the list
	 * reference itself. The reported <code>oldValue</code> will be the object
	 * that was removed.
	 * 
	 * @param propertyName
	 *            The name of the collection property that was changed
	 * @param removedElement
	 *            The element that was removed from the collection
	 */
	protected void fireItemRemovedFromCollection(String propertyName, Object removedElement) {
		listeners.firePropertyChange(propertyName + "#removed", removedElement, null);
	}

}
