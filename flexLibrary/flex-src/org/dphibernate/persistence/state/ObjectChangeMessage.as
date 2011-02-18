package org.dphibernate.persistence.state
{
	import mx.collections.ArrayCollection;

    [RemoteClass(alias="org.dphibernate.persistence.state.ObjectChangeMessage")]
    public class ObjectChangeMessage
    {
        public function ObjectChangeMessage(owner:IHibernateProxyDescriptor=null, isNew:Boolean = false)
        {
            _owner = owner;
            _isNew = isNew;
            _changedPropertiesTable = new Object();
            _isDeleted = false;
        }

        public static function createDeleted(owner:IHibernateProxyDescriptor):ObjectChangeMessage
        {
            var result:ObjectChangeMessage = new ObjectChangeMessage(owner,false);
            result.isDeleted = true;
			return result;
        }

        private var _owner:IHibernateProxyDescriptor;

        public function get owner():IHibernateProxyDescriptor
        {
            return _owner;
        }

        public function set owner(value:IHibernateProxyDescriptor):void
        {
            _owner = value;
        }

        private var _changedPropertiesTable:Object // ofkey :PropertyName , value: PropertyChangeMessage

        public function hasChangedProperty(propertyName:String):Boolean
        {
            return _changedPropertiesTable[propertyName] != null;
        }

        public function getPropertyChange(propertyName:String):PropertyChangeMessage
        {
            return _changedPropertiesTable[propertyName];
        }

        private var _isDeleted:Boolean;

        public function get isDeleted():Boolean
        {
            return _isDeleted;
        }

        public function set isDeleted(value:Boolean):void
        {
            _isDeleted = value;
        }

        private var _isNew:Boolean;

        public function get isNew():Boolean
        {
            return _isNew;
        }

        public function set isNew(value:Boolean):void
        {
			var e:Error = new ReadOnlyError();
            throw e;
        }
		internal function setIsNotNew():void
		{
			_isNew = false;
		}

        private var _changedProperties:ArrayCollection; // Cached table

        public function get changedProperties():ArrayCollection
        {
            if (!_changedProperties)
            {
                var result:ArrayCollection = new ArrayCollection();
                for (var propertyName:String in _changedPropertiesTable)
                {
                    if (_changedPropertiesTable[propertyName] is PropertyChangeMessage)
                    {
                        result.addItem(_changedPropertiesTable[propertyName]);
                    }
                }
                _changedProperties = result;
            }
            return _changedProperties;
        }

        public function set changedProperties(value:ArrayCollection):void
        {
			_changedPropertiesTable = new Object();
			for each (var propertyChange:PropertyChangeMessage in value)
			{
				var propertyName:String = propertyChange.propertyName;
				_changedPropertiesTable[propertyChange] = propertyChange;
			}
			_changedProperties = value;
        }

        public function addChange(change:PropertyChangeMessage):void
        {
			var propertyName:String = change.propertyName;
			if (hasChangedProperty(propertyName))
			{
				var existingChange:PropertyChangeMessage = _changedPropertiesTable[propertyName];
				change = existingChange.merge(change);
			}
            _changedPropertiesTable[propertyName] = change;
			removeChangePropertyIfOldAndNewMatch(propertyName);
            invalidateCache();
        }
		private function removeChangePropertyIfOldAndNewMatch(propertyName:String):void
		{
			var propertyChangeMessage:PropertyChangeMessage = getPropertyChange(propertyName);
			if (!propertyChangeMessage) return;
			if (propertyChangeMessage.oldAndNewValueMatch)
			{
				removeChangeForProperty(propertyName);
			}
		}
        public function removeChangeForProperty(propertyName:String):PropertyChangeMessage
        {
            var existingChange:PropertyChangeMessage = _changedPropertiesTable[propertyName];
            delete _changedPropertiesTable[propertyName];
            invalidateCache();
            return existingChange;
        }
		/**
		 * Removes an explicit PropertyChangeMessage
		 * Note - this method differs from removeChangeForProperty in that
		 * comparison is done on the UID for a property change message, rather than
		 * on the name of the property.
		 * This is useful when removing a propertyChangeMessage after it has
		 * been processed by the server, as if a subsequent change has been made to the
		 * property, it won't be affected */ 
		public function removePropertyChangeMessage(messageToRemove:PropertyChangeMessage):Boolean
		{
			if (!_changedPropertiesTable) return false;
			var propertyName:String = messageToRemove.propertyName;
			var localPropertyMessage:PropertyChangeMessage = _changedPropertiesTable[propertyName];
			if (!localPropertyMessage)
			{
				return true;
			}
			if (localPropertyMessage.uid == messageToRemove.uid)
			{
				removeChangeForProperty(propertyName);
				return true;
			}
			return false;
		}
		public function removePropertyChangeMessages(messagesToRemove:Array):void
		{
			for each (var propertyChangeMessage:PropertyChangeMessage in messagesToRemove)
			{
				removePropertyChangeMessage(propertyChangeMessage);
			}
		}
		public function markPropertyChangeMessagesUpdated(messages:ArrayCollection):void
		{
			for each ( var message:PropertyChangeMessage in messages)
			{
				var propertyWasRemoved:Boolean = removePropertyChangeMessage(message);
				if (!propertyWasRemoved) 
				{
					var existingPropertyChangeMessage:PropertyChangeMessage = getPropertyChange(message.propertyName);
					_changedPropertiesTable[message.propertyName] = message.mergeOldValue(existingPropertyChangeMessage);
				}
			}
		}
		
        private function invalidateCache():void
        {
            _changedProperties = null;
        }

        public function getChangeAt(index:int):PropertyChangeMessage
        {
            return changedProperties[index];
        }

        public function get numChanges():int
        {
            return changedProperties.length;
        }

        public function get hasChanges():Boolean
        {
            return numChanges > 0;
        }
		
		public function clone():ObjectChangeMessage
		{
			var clone:ObjectChangeMessage = new ObjectChangeMessage(owner,isNew);
			clone._changedPropertiesTable = cloneChangedPropertiesTable();
			clone._isDeleted = _isDeleted;
			return clone;			
		}
		private function cloneChangedPropertiesTable():Object
		{
			var clone:Object = new Object();
			for (var propertyName:String in _changedPropertiesTable)
			{
				clone[propertyName] = _changedPropertiesTable[propertyName];
			}
			return clone;
		}
    }
}