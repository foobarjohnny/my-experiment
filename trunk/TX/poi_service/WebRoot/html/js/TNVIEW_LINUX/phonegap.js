if (typeof(DeviceInfo) != 'object')
    DeviceInfo = {};

/**
 * This represents the PhoneGap API itself, and provides a global namespace for accessing
 * information about the state of PhoneGap.
 * @class
 */
PhoneGap = {
    queue: {
        ready: true,
        commands: [],
        timer: null
    },
    _constructors: []
};
/**
 * Boolean flag indicating if the PhoneGap API is available and initialized.
 */ // TODO: Remove this, it is unused here ... -jm
PhoneGap.available = DeviceInfo.uuid != undefined;

/**
 * Add an initialization function to a queue that ensures it will run and initialize
 * application constructors only once PhoneGap has been initialized.
 * @param {Function} func The function callback you want run once PhoneGap is initialized
 */
PhoneGap.addConstructor = function(func) {
    //FIXME
    var state = document.readyState;
    if( (state == 'loaded' || state == 'complete' ) && DeviceInfo.uuid != null)
	{
		func();
	}
    else
	{
        PhoneGap._constructors.push(func);
	}
};

(function() 
 {
    var timer = setInterval(function()
	{
							
		var state = document.readyState;
							
        if ( ( state == 'loaded' || state == 'complete' ) && DeviceInfo.uuid != null )
		{
			clearInterval(timer); // stop looking
			// run our constructors list
			while (PhoneGap._constructors.length > 0) 
			{
				var constructor = PhoneGap._constructors.shift();
				try 
				{
					constructor();
				} 
				catch(e) 
				{
					if (typeof(debug['log']) == 'function')
					{
						debug.log("Failed to run constructor: " + debug.processMessage(e));
					}
					else
					{
						alert("Failed to run constructor: " + e.message);
					}
				}
            }
			// all constructors run, now fire the deviceready event
			var e = document.createEvent('Events'); 
			e.initEvent('deviceready');
			document.dispatchEvent(e);
		}
    }, 1);
})();


// centralized callbacks
PhoneGap.callbackId = 0;
PhoneGap.callbacks = {};
PhoneGap.callbackStatus = {
    NO_RESULT: 0,
    OK: 1,
    CLASS_NOT_FOUND_EXCEPTION: 2,
    ILLEGAL_ACCESS_EXCEPTION: 3,
    INSTANTIATION_EXCEPTION: 4,
    MALFORMED_URL_EXCEPTION: 5,
    IO_EXCEPTION: 6,
    INVALID_ACTION: 7,
    JSON_EXCEPTION: 8,
    ERROR: 9
    };
/**
 * Execute a PhoneGap command in a queued fashion, to ensure commands do not
 * execute with any race conditions, and only run when PhoneGap is ready to
 * recieve them.
 * @param {String} command Command to be run in PhoneGap, e.g. "ClassName.method"
 * @param {String[]} [args] Zero or more arguments to pass to the method
 */
PhoneGap.exec = function() {
//    alert("in PhoneGap.exec");
    PhoneGap.queue.commands.push(arguments);
    if(PhoneGap.queue.timer!=null)
    {
 //       alert("length:"+PhoneGap.queue.commands.length);
    }
    if (PhoneGap.queue.timer == null)
        PhoneGap.queue.timer = setInterval(PhoneGap.run_command, 10);
};

/**
 * Internal function used to dispatch the request to PhoneGap.  It processes the
 * command queue and executes the next command on the list.  If one of the
 * arguments is a JavaScript object, it will be passed on the QueryString of the
 * url, which will be turned into a dictionary on the other end.
 * @private
 */
PhoneGap.run_command = function() {

    if (!PhoneGap.available || !PhoneGap.queue.ready)
    {
       alert("--PhoneGap.available--"+PhoneGap.available);
        return;
    }
    PhoneGap.queue.ready = false;
    var args = PhoneGap.queue.commands.shift();
    if (PhoneGap.queue.commands.length == 0) {
        clearInterval(PhoneGap.queue.timer);
        PhoneGap.queue.timer = null;
    }
	var service;
	var callbackId = null;
    try {
		service = args[0];
		var success = args[1];
		var fail = args[2];
		callbackId = service + PhoneGap.callbackId++;
		if (success || fail) {
			PhoneGap.callbacks[callbackId] = {success:success, fail:fail};
		}
		localAppNativeCallRequest(callbackId,service,JSON.stringify(args[3]));
	}catch (e) {
        console.log("Error: "+e);
    }

};

/**
 * Called by native code when returning successful result from an action.
 *
 * @param callbackId
 * @param args
 *		args.status - PhoneGap.callbackStatus
 *		args.message - return value
 *		args.keepCallback - 0 to remove callback, 1 to keep callback in PhoneGap.callbacks[]
 */
PhoneGap.callbackSuccess = function(callbackId, args) {
    if (PhoneGap.callbacks[callbackId]) {

        // If result is to be sent to callback
        if (args.status == PhoneGap.callbackStatus.OK) {
            try {
				var jsonret = JSON.parse(args.message);
                if (PhoneGap.callbacks[callbackId].success) {
					if(callbackId.indexOf("Geolocation.getCurrentPosition") != -1 && Math.abs(jsonret.coords.latitude)>1E4 ){
						jsonret.coords.latitude /= 100000;
						jsonret.coords.longitude /= 100000;
					}
					PhoneGap.callbacks[callbackId].success(jsonret);
                }
            }
            catch (e) {
                console.log("Error in success callback: "+callbackId+" = "+e);
            }
        }
    
        // Clear callback if not expecting any more results
        /*
        if (!args.keepCallback) {
            delete PhoneGap.callbacks[callbackId];
        }
        */
    }
};

/**
 * Called by native code when returning error result from an action.
 *
 * @param callbackId
 * @param args
 */
PhoneGap.callbackError = function(callbackId, args) {
    if (PhoneGap.callbacks[callbackId]) {
        try {
            if (PhoneGap.callbacks[callbackId].fail) {
                PhoneGap.callbacks[callbackId].fail(args.message);
            }
        }
        catch (e) {
            console.log("Error in error callback: "+callbackId+" = "+e);
        }
        
        // Clear callback if not expecting any more results
       // if (!args.keepCallback) {
       //     delete PhoneGap.callbacks[callbackId];
       // }
    }
};



/**
 * This class contains acceleration information
 * @constructor
 * @param {Number} x The force applied by the device in the x-axis.
 * @param {Number} y The force applied by the device in the y-axis.
 * @param {Number} z The force applied by the device in the z-axis.
 */
function Acceleration(x, y, z) {
	/**
	 * The force applied by the device in the x-axis.
	 */
	this.x = x;
	/**
	 * The force applied by the device in the y-axis.
	 */
	this.y = y;
	/**
	 * The force applied by the device in the z-axis.
	 */
	this.z = z;
	/**
	 * The time that the acceleration was obtained.
	 */
	this.timestamp = new Date().getTime();
}

/**
 * This class specifies the options for requesting acceleration data.
 * @constructor
 */
function AccelerationOptions() {
	/**
	 * The timeout after which if acceleration data cannot be obtained the errorCallback
	 * is called.
	 */
	this.timeout = 10000;
}
/**
 * This class provides access to device accelerometer data.
 * @constructor
 */
function Accelerometer() 
{
	/**
	 * The last known acceleration.
	 */
	this.lastAcceleration = new Acceleration(0,0,0);
}

/**
 * Asynchronously aquires the current acceleration.
 * @param {Function} successCallback The function to call when the acceleration
 * data is available
 * @param {Function} errorCallback The function to call when there is an error 
 * getting the acceleration data.
 * @param {AccelerationOptions} options The options for getting the accelerometer data
 * such as timeout.
 */
Accelerometer.prototype.getCurrentAcceleration = function(successCallback, errorCallback, options) {
	// If the acceleration is available then call success
	// If the acceleration is not available then call error
	
	// Created for iPhone, Iphone passes back _accel obj litteral
	if (typeof successCallback == "function") {
		successCallback(this.lastAcceleration);
	}
}

// private callback called from Obj-C by name
Accelerometer.prototype._onAccelUpdate = function(x,y,z)
{
   this.lastAcceleration = new Acceleration(x,y,z);
}

/**
 * Asynchronously aquires the acceleration repeatedly at a given interval.
 * @param {Function} successCallback The function to call each time the acceleration
 * data is available
 * @param {Function} errorCallback The function to call when there is an error 
 * getting the acceleration data.
 * @param {AccelerationOptions} options The options for getting the accelerometer data
 * such as timeout.
 * @returns {Integer} the ID of the timer
 */

Accelerometer.prototype.watchAcceleration = function(successCallback, errorCallback, options) {
	//this.getCurrentAcceleration(successCallback, errorCallback, options);
	// TODO: add the interval id to a list so we can clear all watches
 	var frequency = (options != undefined && options.frequency != undefined) ? options.frequency : 10000;
	var updatedOptions = {
		desiredFrequency:frequency 
	}
	PhoneGap.exec("Accelerometer.start",options);

	return setInterval(function() {
		navigator.accelerometer.getCurrentAcceleration(successCallback, errorCallback, options);
	}, frequency);
}

/**
 * Clears the specified accelerometer watch.
 * @param {String} watchId The ID of the watch returned from #watchAcceleration.
 */
Accelerometer.prototype.clearWatch = function(watchId) {
	PhoneGap.exec("Accelerometer.stop");
	clearInterval(watchId);
}

PhoneGap.addConstructor(function() {
    if (typeof navigator.accelerometer == "undefined") navigator.accelerometer = new Accelerometer();
});


/**
 * This class provides access to the device camera.
 * @constructor
 */
function Camera() {
	
}

/**
 * Get the picture from camera
 * @param {Function} successCallback
 * @param {Function} errorCallback
 * @param {Object} options
 */
Camera.prototype.getPicture = function(successCallback, errorCallback, options) {
	PhoneGap.exec("Camera.getPicture", GetFunctionName(successCallback), GetFunctionName(errorCallback), options);
}

/** 
 * Defines integers to match iPhone UIImagePickerControllerSourceType enum
*/
Camera.prototype.PictureSourceType = {
		PHOTOLIBRARY : 0,
		CAMERA : 1,
		SAVEDPHOTOALBUM : 2
};
/**
 * Format of image that returned from getPicture.
 *
 * @example: navigator.camera.getPicture(success, fail,
 *              { quality: 80,
 *                destinationType: Camera.DestinationType.DATA_URL,
 *                sourceType: Camera.PictureSourceType.PHOTOLIBRARY})
 */
Camera.DestinationType = {
    DATA_URL: 0,                // Return base64 encoded string
    FILE_URI: 1                 // Return file uri 
};
Camera.prototype.DestinationType = Camera.DestinationType;


PhoneGap.addConstructor(function() {
    if (typeof navigator.camera == "undefined") navigator.camera = new Camera();
});



/**
 * This class store the contact data.
 * 
 * @constructor
 */

function Contact(jsonObject) {
	this.firstName = "";
	this.lastName = "";
    this.name = "";
    this.phones = {};
    this.emails = {};
	this.address = "";
}

Contact.prototype.displayName = function()
{
    // TODO: can be tuned according to prefs
	return this.name;
}
/**
 * This class provides access to the device contacts.
 * 
 * @constructor
 */
function ContactManager() {
	// Dummy object to hold array of contacts
	this.contacts = [];
	this.timestamp = new Date().getTime();
}
/**
 * Get all the contacts
 * @param {Function} successCallback
 * @param {Function} errorCallback
 * @param {Object} options
 */
ContactManager.prototype.getAllContacts = function(successCallback, errorCallback, options) {
	PhoneGap.exec("Contacts.allContacts", GetFunctionName(successCallback), options);
}

// THE FUNCTIONS BELOW ARE iPHONE ONLY FOR NOW
/**
 * Create a contact
 * @param {Contact} contact
 * @param {Function} successCallback
 * @param {Object} options
 */
ContactManager.prototype.newContact = function(contact, successCallback, options) {
    if (!options) options = {};
    options.successCallback = GetFunctionName(successCallback);
    
    PhoneGap.exec("Contacts.newContact", contact.firstName, contact.lastName, contact.phoneNumber,
        options);
}
/**
 * Choose a contact
 * @param {Function} successCallback
 * @param {Object} options
 */
ContactManager.prototype.chooseContact = function(successCallback, options) {
    PhoneGap.exec("Contacts.chooseContact", GetFunctionName(successCallback), options);
}
/**
 * Display a contact
 * @param {Integer} contactID
 * @param {Function} errorCallback
 * @param {Object} options
 */
ContactManager.prototype.displayContact = function(contactID, errorCallback, options) {
    PhoneGap.exec("Contacts.displayContact", contactID, GetFunctionName(errorCallback), options);
}
/**
 * Remove a contact
 * @param {Integer} contactID
 * @param {Function} successCallback
 * @param {Object} options
 */
ContactManager.prototype.removeContact = function(contactID, successCallback, options) {
    PhoneGap.exec("Contacts.removeContact", contactID, GetFunctionName(successCallback), options);
}
/**
 * get the count of all contacts
 * @param {Function} successCallback the function that accept a number as input
 * @param {Function} errorCallback
 */
ContactManager.prototype.contactsCount = function(successCallback, errorCallback) {
	PhoneGap.exec("Contacts.contactsCount", GetFunctionName(successCallback));
}

PhoneGap.addConstructor(function() {
    if (typeof navigator.contacts == "undefined") navigator.contacts = new ContactManager();
});
/**
 * This class provides access to the debugging console.
 * @constructor
 */
function DebugConsole(isDeprecated) {
    this.logLevel = DebugConsole.INFO_LEVEL;
    this.isDeprecated = isDeprecated ? true : false;
}

// from most verbose, to least verbose
DebugConsole.ALL_LEVEL    = 1; // same as first level
DebugConsole.INFO_LEVEL   = 1;
DebugConsole.WARN_LEVEL   = 2;
DebugConsole.ERROR_LEVEL  = 4;
DebugConsole.NONE_LEVEL   = 8;
													
DebugConsole.prototype.setLevel = function(level) {
    this.logLevel = level;
}

/**
 * Utility function for rendering and indenting strings, or serializing
 * objects to a string capable of being printed to the console.
 * @param {Object|String} message The string or object to convert to an indented string
 * @private
 */
DebugConsole.prototype.processMessage = function(message) {
    if (typeof(message) != 'object') {
        return (this.isDeprecated ? "WARNING: debug object is deprecated, please use console object \n" + message : message);
    } else {
        /**
         * @function
         * @ignore
         */
        function indent(str) {
            return str.replace(/^/mg, "    ");
        }
        /**
         * @function
         * @ignore
         */
        function makeStructured(obj) {
            var str = "";
            for (var i in obj) {
                try {
                    if (typeof(obj[i]) == 'object') {
                        str += i + ":\n" + indent(makeStructured(obj[i])) + "\n";
                    } else {
                        str += i + " = " + indent(String(obj[i])).replace(/^    /, "") + "\n";
                    }
                } catch(e) {
                    str += i + " = EXCEPTION: " + e.message + "\n";
                }
            }
            return str;
        }
        
        return ((this.isDeprecated ? "WARNING: debug object is deprecated, please use console object\n" :  "") + "Object:\n" + makeStructured(message));
    }
};

/**
 * Print a normal log message to the console
 * @param {Object|String} message Message or object to print to the console
 */
DebugConsole.prototype.log = function(message) {
    if (PhoneGap.available && this.logLevel <= DebugConsole.INFO_LEVEL)
        PhoneGap.exec('DebugConsole.log',
            this.processMessage(message),
            { logLevel: 'INFO' }
        );
    else
        console.log(message);
};

/**
 * Print a warning message to the console
 * @param {Object|String} message Message or object to print to the console
 */
DebugConsole.prototype.warn = function(message) {
    if (PhoneGap.available && this.logLevel <= DebugConsole.WARN_LEVEL)
        PhoneGap.exec('DebugConsole.log',
            this.processMessage(message),
            { logLevel: 'WARN' }
        );
    else
        console.error(message);
};

/**
 * Print an error message to the console
 * @param {Object|String} message Message or object to print to the console
 */
DebugConsole.prototype.error = function(message) {
    if (PhoneGap.available && this.logLevel <= DebugConsole.ERROR_LEVEL)
        PhoneGap.exec('DebugConsole.log',
            this.processMessage(message),
            { logLevel: 'ERROR' }
        );
    else
        console.error(message);
};

PhoneGap.addConstructor(function() {
   // window.console = new DebugConsole();
    //window.debug = new DebugConsole(true);
});
/**
 * this represents the mobile device, and provides properties for inspecting the model, version, UUID of the
 * phone, etc.
 * @constructor
 */
function Device() 
{
    this.platform = null;
    this.version  = null;
    this.name     = null;
    this.phonegap      = null;
    this.uuid     = null;
    try 
	{      
		this.platform = DeviceInfo.platform;
		this.version  = DeviceInfo.version;
		this.name     = DeviceInfo.name;
		this.phonegap = DeviceInfo.gap;
		this.uuid     = DeviceInfo.uuid;

    } 
	catch(e) 
	{
        // TODO: 
    }
	this.available = PhoneGap.available = this.uuid != null;
}

PhoneGap.addConstructor(function() {
    navigator.device = window.device = new Device();
});


PhoneGap.addConstructor(function() { if (typeof navigator.fileMgr == "undefined") navigator.fileMgr = new FileMgr();});


// File error codes
// Found in DOMException ( 1-3 )
// Added by this specification ( 4 - 8 )

FileError = {
    NOT_IMPLEMENTED:-1,
    NOT_FOUND_ERR:1,
    SECURITY_ERR:2,
    ABORT_ERR:3,
    NOT_READABLE_ERR:4,
    ENCODING_ERR:5,
    NO_MODIFICATION_ALLOWED_ERR:6,
    INVALID_STATE_ERR:7,
    SYNTAX_ERR:8
};
/**
 * This class provides generic read and write access to the mobile device file system.
 * They are not used to read files from a server.
 */

/**
 * List of files
 */
function FileList() {
    this.files = {};
};

/**
 * Describes a single file in a FileList
 */
function File() {
    this.name = null;
    this.type = null;
    this.urn = null;
};
/**
 * Create an event object since we can't set target on DOM event.
 *
 * @param type
 * @param target
 *
 */
File._createEvent = function(type, target) {
    // Can't create event object, since we can't set target (its readonly)
    //var evt = document.createEvent('Events');
    //evt.initEvent("onload", false, false);
    var evt = {"type": type};
    evt.target = target;
    return evt;
};
    


/**
 * This class provides iPhone read and write access to the mobile device file system.
 * Based loosely on http://www.w3.org/TR/2009/WD-FileAPI-20091117/#dfn-empty
 */
function FileMgr() 
{
	//this.getFileBasePaths();
	//this.getFreeDiskSpace();
}

FileMgr.seperator = "/";

FileMgr.prototype = {
 
 	fileWriters:{},// empty maps
 	
	fileReaders:{},
	
    // these should likely be static :: File.documentsDirectory
	docsFolderPath:"./../Documents/",
	// File.applicationStorageDirectory
	libFolderPath:"./../Library/",
	
	tempFolderPath:"./../tmp/",
	
	freeDiskSpace:-1,
    
    // private, called from Native Code
    _setPaths:function(docs,temp,lib){
        
        
    	this.docsFolderPath = docs;
    	
    	this.tempFolderPath = temp;
    	
        this.libFolderPath = lib;
    
    },
    
    /* coming soon
    resolvePath:function(path){
        
        // app:/
        // app-storage:/
        // 
        
        if(path.indexOf("docs:/") == 0)
        {
            
        }
        else if(path.indexOf("lib:/") == 0)
        {
            
        }
        else if(path.indexOf("tmp:/") == 0)
        {
            
        }
        else
        {
            
        }
        
    },
    */

    // private, called from Native Code
    _setFreeDiskSpace:function(val){
    	this.freeDiskSpace = val;
    },

    // FileWriters add/remove
    // called internally by writers
    addFileWriter:function(filePath,fileWriter){
    	this.fileWriters[filePath] = fileWriter;
    	return fileWriter;
    },

    removeFileWriter:function(filePath){
    	this.fileWriters[filePath] = null;
    },

    // File readers add/remove
    // called internally by readers
    addFileReader:function(filePath,fileReader){
    	this.fileReaders[filePath] = fileReader;
    	return fileReader;
    },

    removeFileReader:function(filePath){
    	this.fileReaders[filePath] = null;
    },
    
    /*******************************************
     *
     *	private reader callback delegation
     *	called from native code
     */
    reader_onloadstart:function(filePath,result)
    {
    	this.fileReaders[filePath].result = unescape(result);
    	var evt = File._createEvent("loadstart", this.fileReaders[filePath]);
    	this.fileReaders[filePath].onloadstart(evt);
    },

    reader_onprogress:function(filePath,result){
    	this.fileReaders[filePath].result = unescape(result);
    	var evt = File._createEvent("progress", this.fileReaders[filePath]);
    	this.fileReaders[filePath].onprogress(evt);
    },

    reader_onload:function(filePath,result){
    	this.fileReaders[filePath].result = unescape(result);
    	var evt = File._createEvent("load", this.fileReaders[filePath]);
    	this.fileReaders[filePath].onload(evt);
    },

    reader_onerror:function(filePath,err){
    	//this.fileReaders[filePath].result = err;
    	this.fileReaders[filePath].result = unescape(err);
    	var evt = File._createEvent("error", this.fileReaders[filePath]);
    	this.fileReaders[filePath].onerror(evt);
    },

    reader_onloadend:function(filePath,result){
        this.fileReaders[filePath].result = unescape(result);
        var evt = File._createEvent("loadend", this.fileReaders[filePath]);
    	this.fileReaders[filePath].onloadend(evt);
    },
    
    /*******************************************
     *
     *	private writer callback delegation
     *	called from native code
    */
    writer_onerror:function(filePath,err){
        this.fileWriters[filePath].error = err;
    	this.fileWriters[filePath].onerror(err);
    },

    writer_oncomplete:function(filePath,result) {

        var writer = this.fileWriters[filePath];
        writer.length = result;
        writer.position = result;

        var evt = File._createEvent("writeend", writer);
        writer.onwriteend(evt);

        evt.type = "complete";
    	writer.oncomplete(evt); // result contains bytes written
    },
    
    
    // Public interface
    

    
    getRootPaths:function(){
        return [ this.docsFolderPath, this.libFolderPath, this.tempFolderPath];
    }
}


/**
 * Read a file as text
 * @param {String} fileName the name of the file to read
 * @param {String} encoding Specifies output format of the content of the file 
 * @param {Function}  successCallback the name of the success function
 * @param {Function}  errorCallback the name of the error function
 */
FileMgr.prototype.readAsText = function(fileName, encoding, successCallback, errorCallback)
{
    var textreader = new FileReader();

    textreader.onload = successCallback;
    textreader.onerror = errorCallback;
 
    textreader.readAsText(fileName);
}
/**
 * Write a file as text
 * @param {String} fileName the name of the file to write
 * @param {String} data The content that will be wrote to the file  
 * @param {String} indicates whether to append data to the end of the file 
 * @param {Function} successCallback the name of the success function
 * @param {Function} errorCallback the name of the error function
 */
FileMgr.prototype.writeAsText = function(fileName, data, append, successCallback, errorCallback)
{
    var wr = new FileWriter();
    wr.oncomplete=successCallback;	
    wr.onerror=errorCallback;
    wr.writeAsText(fileName,data,append);
}




/**
 * Get the File Base Paths
 * to Get the paths, visit the FileMgr members of paths
 */
FileMgr.prototype.getFileBasePaths = function()
{
	PhoneGap.exec("File.getFileBasePaths");
}
/**
 * Test whether the file exists
 * @param {String} fileName the name of the file to test
 * @param {Function} win the name of the success function
 * @param {Function} fail the name of the error function
 */
FileMgr.prototype.testFileExists = function(fileName, win, fail)
{
	this.successCallback = function(b){win(b);};
	this.errorCallback = function(b){fail(b);};
	PhoneGap.exec("File.testFileExists",fileName);
}
/**
 * Test whether the directory exists
 * @param {String} dirName the name of the directory to test
 * @param {Function} win the name of the success function
 * @param {Function} fail the name of the error function
 */
FileMgr.prototype.testDirectoryExists = function(dirName, win, fail)
{
	this.successCallback = function(b){win(b);};
	this.errorCallback = function(b){fail(b);};
	PhoneGap.exec("File.testDirectoryExists",dirName);
}
/**
 * Create a directory
 * @param {String} dirName the name of the directory to create
 * @param {Function} successCallback the name of the success function
 * @param {Function} errorCallback the name of the error function
 */
FileMgr.prototype.createDirectory = function(dirName, successCallback, errorCallback)
{
	this.successCallback = successCallback;
	this.errorCallback = errorCallback;
	PhoneGap.exec("File.createDirectory",dirName);
}
/**
 * Delete a directory
 * @param {String} dirName the name of the directory to delete
 * @param {Function} successCallback the name of the success function
 * @param {Function} errorCallback the name of the error function
 */
FileMgr.prototype.deleteDirectory = function(dirName, successCallback, errorCallback)
{
	this.successCallback = successCallback;
	this.errorCallback = errorCallback;
	PhoneGap.exec("File.deleteDirectory",dirName);
}
/**
 * Delete a file
 * @param {String} fileName the name of the file to delete
 * @param {Function} successCallback the name of the success function
 * @param {Function} errorCallback the name of the error function
 */
FileMgr.prototype.deleteFile = function(fileName, successCallback, errorCallback)
{
	this.successCallback = successCallback;
	this.errorCallback = errorCallback;
	PhoneGap.exec("File.deleteFile",fileName);
}
/**
 * Get the free disk space(in Bytes)
 * @param {Function} successCallback the name of the success function
 * @param {Function} errorCallback the name of the error function
 */
FileMgr.prototype.getFreeDiskSpace = function(successCallback, errorCallback)
{
	if(this.freeDiskSpace > 0)
	{
        successCallback(this.freeDiskSpace);
		return this.freeDiskSpace;
	}
	else
	{
		this.successCallback = successCallback;
		this.errorCallback = errorCallback;
		PhoneGap.exec("File.getFreeDiskSpace");
	}
}



//*******************************  File Reader

function FileReader(filename){this.fileName = filename;}
// States
FileReader.EMPTY = 0;
FileReader.LOADING = 1;
FileReader.DONE = 2;

FileReader.prototype = {
	fileName:null,
	result:null,
	onloadstart:null,
	onprogress:null,
	onload:null,
	onerror:null,
	onloadend:null,
	abort:function(){
	    
	},
	
	readAsBinaryString:function(filename){
	    // TODO - Can't return binary data to browser.
	},
	
	readAsDataURL:function(url){
	    
	},
	
	readAsArrayBuffer:function(filename){
	    // TODO - Can't return binary data to browser.
	}
	
	
}


/**
 * This class provides iPhone read access to the mobile device file system.
 * Based loosely on http://www.w3.org/TR/2009/WD-FileAPI-20091117/#dfn-empty
 */


/**
 * Read the file as text
 * @param {String} file the file name to read
 */
FileReader.prototype.readAsText = function(file)
{
	if(this.fileName && this.fileName.length > 0)
	{
		navigator.fileMgr.removeFileReader(this.fileName,this);
	}
	this.fileName = file;
	navigator.fileMgr.addFileReader(this.fileName,this);
	//alert("Calling File.read : " + this.fileName);
	//window.location = "gap://File.readFile/"+ file;
	PhoneGap.exec("File.readFile",this.fileName);
}

// File Writer
/**
 * This class provides iPhone write access to the mobile device file system.
 * Based loosely on http://www.w3.org/TR/2009/WD-FileAPI-20091117/#dfn-empty
 */
 function FileWriter(filename) 
{ 
    if(navigator.fileMgr.fileWriters[filename] != null)
    {
        return navigator.fileMgr.fileWriters[filename];
    }
    else 
    {
        this.fileName = filename;
    }
}

// States
FileWriter.INIT = 0;
FileWriter.WRITING = 1;
FileWriter.DONE = 2;

FileWriter.prototype = {

	fileName:"",
	result:null,
	readyState:0, // 0 | 1 | 2 == INIT | WRITING | DONE
	onerror:null,
	oncomplete:null,
	onwritestart:null,
	onprogress:null,
	onload:null,
	onabort:null,
	onerror:null,
	onwriteend:null,
	length:0,  // readonly
	position:0, // readonly
	error:null,
	
	// Writes data to the file.
	write:function(text) 
	{	    
	    return this.writeAsText(this.fileName,text,false);
	},
	
	// Shortens the file to the length specified.
	// Note that length does not change postition UNLESS position has become invalid
	truncate:function(offset){
	    
	    //alert("truncate" + this.fileName);
        if(this.readyState == FileWriter.WRITING)
	    {
	        throw FileError.INVALID_STATE_ERR;
	    }
	    
	    // WRITING state
        this.readyState = FileWriter.WRITING;
	    
    	if(this.fileName && this.fileName.length > 0)
    	{
    		navigator.fileMgr.removeFileWriter(this.fileName);
    	}
    	
    	navigator.fileMgr.addFileWriter(this.fileName,this);
    	this.readyState = 0; // EMPTY
    	this.result = null;
    	PhoneGap.exec("File.truncateFile",this.fileName,offset);	
	},
	
	// Moves the file pointer to the byte specified.
	seek:function(offset){
	    // Throw an exception if we are already writing a file
          if (this.readyState === FileWriter.WRITING) {
              throw FileError.INVALID_STATE_ERR;
          }

          if (!offset) {
              return;
          }
          
          // Seek back from end of file.
              if (offset < 0) {
          		this.position = Math.max(offset + this.length, 0);
          	}
              // Offset is bigger then file size so set position 
              // to the end of the file.
          	else if (offset > this.length) {
          		this.position = this.length;
          	}
              // Offset is between 0 and file size so set the position
              // to start writing.
          	else {
          		this.position = offset;
          	}
	},
	
	
/* http://www.w3.org/TR/2010/WD-file-writer-api-20101026/#widl-FileWriter-write
1. If readyState is DONE or INIT, throw a FileException with error code INVALID_STATE_ERR and terminate this overall series of steps.
2. Terminate any steps having to do with writing a file.
3. Set the error attribute to a FileError object with the appropriate code (in this case, ABORT_ERR; see error conditions).
4. Dispatch a progress event called error.
5. Dispatch a progress event called abort
6. Set readyState to DONE.
7. Dispatch a progress event called writeend
8. Stop dispatching any further progress events.
*/ 
//Aborts writing file.
	abort:function(){

	    if(this.readyState != FileWriter.WRITING)
	    {
	        throw FileError.INVALID_STATE_ERR;
	    }
	    
	    var error = new FileError();
            error.code = FileError.ABORT_ERR;
            this.error = error;
            
        // If error callback
        if (typeof this.onerror == "function") {
            var evt = File._createEvent("error", this);
            this.onerror(evt);
        }
        // If abort callback
        if (typeof this.onabort == "function") {
            var evt = File._createEvent("abort", this);
            this.onabort(evt);
        }

        this.readyState = FileWriter.DONE;

        // If load end callback
        if (typeof this.onloadend == "function") {
            var evt = File._createEvent("writeend", this);
            this.onloadend(evt);
        }
	}
}




/**
 * Write the file as text
 * @param {String} file the file name to read
 * @param {String} text the content need to write
 * @param {Bool} bAppend the append flag, 1 for append and 0 for not
 */
FileWriter.prototype.writeAsText = function(fname,text,bAppend)
{
													
	if(this.readyState == FileWriter.WRITING)
	    {
	        throw FileError.INVALID_STATE_ERR;
	    }
	    
	    // WRITING state
        this.readyState = FileWriter.WRITING;
	    
    	if(this.fileName && this.fileName.length > 0)
    	{
    		navigator.fileMgr.removeFileWriter(this.fileName);
    	}
													
    	this.fileName = fname;

    	navigator.fileMgr.addFileWriter(this.fileName,this);
    	this.readyState = 0; // EMPTY
    	this.result = null;
		if(bAppend==false)
			this.position = 0;
    	PhoneGap.exec("File.write",this.fileName,text,this.position);
}


/**
 * This class provides access to device Compass data.
 * @constructor
 */
function Compass() {
    /**
     * The last known Compass position.
     */
	this.lastHeading = null;
    this.lastError = null;
	this.callbacks = {
		onHeadingChanged: [],
        onError:           []
    };
};

/**
 * Asynchronously aquires the current heading.
 * @param {Function} successCallback The function to call when the heading
 * data is available
 * @param {Function} errorCallback The function to call when there is an error 
 * getting the heading data.
 * @param {PositionOptions} options The options for getting the heading data
 * such as timeout.
 */
Compass.prototype.getCurrentHeading = function(successCallback, errorCallback, options) {
	if (this.lastHeading == null) {
		this.start(options);
	}
	else 
	if (typeof successCallback == "function") {
		successCallback(this.lastHeading);
	}
};

/**
 * Asynchronously aquires the heading repeatedly at a given interval.
 * @param {Function} successCallback The function to call each time the heading
 * data is available
 * @param {Function} errorCallback The function to call when there is an error 
 * getting the heading data.
 * @param {HeadingOptions} options The options for getting the heading data
 * such as timeout and the frequency of the watch.
 */
Compass.prototype.watchHeading= function(successCallback, errorCallback, options) {
	// Invoke the appropriate callback with a new Position object every time the implementation 
	// determines that the position of the hosting device has changed. 
	
	this.getCurrentHeading(successCallback, errorCallback, options);
	var frequency = 100;
    if (typeof(options) == 'object' && options.frequency)
        frequency = options.frequency;

	var self = this;
	return setInterval(function() {
		self.getCurrentHeading(successCallback, errorCallback, options);
	}, frequency);
};


/**
 * Clears the specified heading watch.
 * @param {String} watchId The ID of the watch returned from #watchHeading.
 */
Compass.prototype.clearWatch = function(watchId) {
	clearInterval(watchId);
};


/**
 * Called by the geolocation framework when the current heading is found.
 * @param {HeadingOptions} position The current heading.
 */
Compass.prototype.setHeading = function(heading) {
    this.lastHeading = heading;
    for (var i = 0; i < this.callbacks.onHeadingChanged.length; i++) {
        var f = this.callbacks.onHeadingChanged.shift();
        f(heading);
    }
};

/**
 * Called by the geolocation framework when an error occurs while looking up the current position.
 * @param {String} message The text of the error message.
 */
Compass.prototype.setError = function(message) {
    this.lastError = message;
    for (var i = 0; i < this.callbacks.onError.length; i++) {
        var f = this.callbacks.onError.shift();
        f(message);
    }
};

Compass.prototype.start = function(args) {
    PhoneGap.exec("Location.startHeading", args);
};

Compass.prototype.stop = function() {
    PhoneGap.exec("Location.stopHeading");
};

PhoneGap.addConstructor(function() {
    if (typeof navigator.compass == "undefined") navigator.compass = new Compass();
});

/**
 * Media/Audio override.
 * @param {String} src the media source path 
 * @param {Function} successCallback
 * @param {Function} errorCallback
 * @param {Function} downloadCompleteCallback
 * @constructor
 */
 
function Media(src, successCallback, errorCallback, statusCallback, positionCallback,downloadCompleteCallback) {
	
	if (!src) {
		src = "documents://" + String((new Date()).getTime()).replace(/\D/gi,''); // random
	}
	this.src = src;
	this.successCallback = successCallback;
	this.errorCallback = errorCallback;	
	this.downloadCompleteCallback = downloadCompleteCallback;
    
	if (this.src != null) {
		PhoneGap.exec("Sound.prepare", this.src, this.successCallback, this.errorCallback, this.downloadCompleteCallback);
	}
}
/**
 * Play the media.
 * @param {playOptions} options
 */
Media.prototype.play = function(options) {
	if (this.src != null) {
		PhoneGap.exec("Sound.play", this.src, options);
	}
}
/**
 * Pause the media.
 */
Media.prototype.pause = function() {
	if (this.src != null) {
		PhoneGap.exec("Sound.pause", this.src);
	}
}
/**
 * Stop the media.
 */
Media.prototype.stop = function() {
	if (this.src != null) {
		PhoneGap.exec("Sound.stop", this.src);
	}
}
/**
 * start to record the media.
 * @param {startOptions} options
 */
Media.prototype.startRecord = function(options) {
	if (this.src != null) {
		PhoneGap.exec("Sound.startAudioRecord", this.src, options);
	}
}
/**
 * stop recording the media.
 */
Media.prototype.stopRecord = function() {
	if (this.src != null) {
		PhoneGap.exec("Sound.stopAudioRecord", this.src);
	}
}
/**
 * Get duration of an audio file.
 * The duration is only set for audio that is playing, paused or stopped.
 *
 * @return      duration or -1 if not known.
 */
Media.prototype.getDuration = function(success,fail) {
    if (this.src != null) {
        PhoneGap.exec("Sound.getDuration", this.src,success,fail);
    }
};
													
/**
 * Get position of audio.
 *
 * @return
 */
Media.prototype.getCurrentPosition = function(success, fail) {
	if (this.src != null) {
	    PhoneGap.exec("Sound.getCurrentPositionAudio", this.src, success, fail);
	}
};
/**
 * This class contains information about any Media errors.
 * @constructor
 */
function MediaError() {
	this.code = null,
	this.message = "";
}

MediaError.MEDIA_ERR_ABORTED 		= 1;
MediaError.MEDIA_ERR_NETWORK 		= 2;
MediaError.MEDIA_ERR_DECODE 		= 3;
MediaError.MEDIA_ERR_NONE_SUPPORTED = 4;


//if (typeof navigator.audio == "undefined") navigator.audio = new Media(src);
/**
 * This class provides access to notifications on the device.
 */
function Notification() 
{
	this.resultsCallback = null;
};

/**
 * Causes the device to blink a status LED.
 * @param {Integer} count The number of blinks.
 * @param {String} colour The colour of the light.
 */
Notification.prototype.blink = function(count, colour) {
	
};
/**
 * Causes the device to vibrate.
 * @param {Integer} count The millseconds of vibrate.
 */
Notification.prototype.vibrate = function(mills) {
	PhoneGap.exec("Notification.vibrate");
};
/**
 * Causes the device to beep.
 * @param {Integer} count The number of beep.
 * @param {Integer} The volume of beep.
 */
Notification.prototype.beep = function(count, volume) {
	// No Volume yet for the iphone interface
	// We can use a canned beep sound and call that
	new Media('beep.wav').play();
};

/**
 * Open a native alert dialog, with a customizable title and button text.
 *
 * @param {String} message              Message to print in the body of the alert
 * @param {Function} resultCallback     The callback that is called when user clicks on a button. 
 * @param {String} title                Title of the alert dialog (default: Alert)
 * @param {String} buttonLabel          Label for close button
 */
Notification.prototype.alert = function(message, resultCallback, title, buttonLabel) 
{
	var options = {};
	options.title = (title || "Alert");
	options.buttonLabel = (buttonLabel || "OK");
	this.resultsCallback = resultCallback;
	PhoneGap.exec('Notification.alert', message, options);
	return;
};


/**
 * Open a native confirm dialog, with a customizable title and button text.
 * The result that the user selects is returned to the result callback.
 *
 * @param {String} message              Message to print in the body of the alert
 * @param {Function} resultCallback     The callback that is called when user clicks on a button.
 * @param {String} title                Title of the alert dialog (default: Confirm)
 * @param {String} buttonLabels         Comma separated list of the labels of the buttons (default: 'OK,Cancel')
 */
Notification.prototype.confirm = function(message, resultCallback, title, buttonLabels) 
{

	var confirmTitle = title ? title : "Confirm";
	var labels = buttonLabels ? buttonLabels : "OK,Cancel";
	return this.alert(message, resultCallback, confirmTitle, labels);
};

Notification.prototype._alertCallback = function(index)
{
	try {
        this.resultsCallback(index);
    }
    catch (e) {
        console.log("Error in user's result callback: " + e);
    }
};



Notification.prototype.activityStart = function() {
    PhoneGap.exec("Notification.activityStart");
};
Notification.prototype.activityStop = function() {
    PhoneGap.exec("Notification.activityStop");
};

Notification.prototype.loadingStart = function(options) {
    PhoneGap.exec("Notification.loadingStart", options);
};
Notification.prototype.loadingStop = function() {
    PhoneGap.exec("Notification.loadingStop");
};

PhoneGap.addConstructor(function() {
    if (typeof navigator.notification == "undefined") navigator.notification = new Notification();
});

/**
 * This class provides access to the device orientation.
 * @constructor
 */
function Orientation() {
	/**
	 * The current orientation, or null if the orientation hasn't changed yet.
	 */
	this.currentOrientation = null;
}

/**
 * Set the current orientation of the phone.  This is called from the device automatically.
 * 
 * When the orientation is changed, the DOMEvent \c orientationChanged is dispatched against
 * the document element.  The event has the property \c orientation which can be used to retrieve
 * the device's current orientation, in addition to the \c Orientation.currentOrientation class property.
 *
 * @param {Number} orientation The orientation to be set
 */
Orientation.prototype.setOrientation = function(orientation) {
    Orientation.currentOrientation = orientation;
    var e = document.createEvent('Events');
    e.initEvent('orientationChanged', 'false', 'false');
    e.orientation = orientation;
    document.dispatchEvent(e);
};

/**
 * Asynchronously aquires the current orientation.
 * @param {Function} successCallback The function to call when the orientation
 * is known.
 * @param {Function} errorCallback The function to call when there is an error 
 * getting the orientation.
 */
Orientation.prototype.getCurrentOrientation = function(successCallback, errorCallback) {
	// If the position is available then call success
	// If the position is not available then call error
};

/**
 * Asynchronously aquires the orientation repeatedly at a given interval.
 * @param {Function} successCallback The function to call each time the orientation
 * data is available.
 * @param {Function} errorCallback The function to call when there is an error 
 * getting the orientation data.
 */
Orientation.prototype.watchOrientation = function(successCallback, errorCallback) {
	// Invoke the appropriate callback with a new Position object every time the implementation 
	// determines that the position of the hosting device has changed. 
	this.getCurrentPosition(successCallback, errorCallback);
	return setInterval(function() {
		navigator.orientation.getCurrentOrientation(successCallback, errorCallback);
	}, 10000);
};

/**
 * Clears the specified orientation watch.
 * @param {String} watchId The ID of the watch returned from #watchOrientation.
 */
Orientation.prototype.clearWatch = function(watchId) {
	clearInterval(watchId);
};

PhoneGap.addConstructor(function() {
    if (typeof navigator.orientation == "undefined") navigator.orientation = new Orientation();
});

function Position(coords, timestamp) {
	this.coords = coords;
        this.timestamp = new Date().getTime();
}
/**
 * This class contains position information.
 * @param {Object} lat
 * @param {Object} lng
 * @param {Object} acc
 * @param {Object} alt
 * @param {Object} altacc
 * @param {Object} head
 * @param {Object} vel
 * @constructor
 */
function Coordinates(lat, lng, alt, acc, head, vel, altAcc) {
	/**
	 * The latitude of the position.
	 */
	this.latitude = lat;
	/**
	 * The longitude of the position,
	 */
	this.longitude = lng;
	/**
	 * The accuracy of the position.
	 */
	this.accuracy = acc;
	/**
	 * The altitude of the position.
	 */
	this.altitude = alt;
	/**
	 * The direction the device is moving at the position.
	 */
	this.heading = head;
	/**
	 * The velocity with which the device is moving at the position.
	 */
	this.speed = vel;
	/**
	 * The altitude accuracy of the position.
	 */
	this.altitudeAccuracy = (altAcc != 'undefined') ? altAcc : null; 
}

/**
 * This class specifies the options for requesting position data.
 * @constructor
 */
function PositionOptions() {
	/**
	 * Specifies the desired position accuracy.
	 */
	this.enableHighAccuracy = true;
	/**
	 * The timeout after which if position data cannot be obtained the errorCallback
	 * is called.
	 */
	this.timeout = 10000;
}

/**
 * This class contains information about any GSP errors.
 * @constructor
 */
function PositionError() {
	this.code = null;
	this.message = "";
}

PositionError.UNKNOWN_ERROR = 0;
PositionError.PERMISSION_DENIED = 1;
PositionError.POSITION_UNAVAILABLE = 2;
PositionError.TIMEOUT = 3;
/**
 * This class provides access to the device SMS functionality.
 * @constructor
 */
function Sms() {

}

/**
 * Sends an SMS message.
 * @param {Integer} number The phone number to send the message to.
 * @param {String} message The contents of the SMS message to send.
 * @param {Function} successCallback The function to call when the SMS message is sent.
 * @param {Function} errorCallback The function to call when there is an error sending the SMS message.
 * @param {PositionOptions} options The options for accessing the GPS location such as timeout and accuracy.
 */
Sms.prototype.send = function(number, message, successCallback, errorCallback, options) {
	
}

PhoneGap.addConstructor(function() {
    if (typeof navigator.sms == "undefined") navigator.sms = new Sms();
});
/**
 * This class provides access to the telephony features of the device.
 * @constructor
 */
function Telephony() {
	
}

/**
 * Calls the specifed number.
 * @param {Integer} number The number to be called.
 */
Telephony.prototype.call = function(number) {
	
}

PhoneGap.addConstructor(function() {
    if (typeof navigator.telephony == "undefined") navigator.telephony = new Telephony();
});


/**
 * This class contains information about any NetworkStatus.
 * @constructor
 */
function NetworkStatus() {
	this.code = null;
	this.message = "";
}

NetworkStatus.NOT_REACHABLE = 0;
NetworkStatus.REACHABLE_VIA_CARRIER_DATA_NETWORK = 1;
NetworkStatus.REACHABLE_VIA_WIFI_NETWORK = 2;

/**
 * This class provides access to device Network data (reachability).
 * @constructor
 */
function Network() {
    /**
     * The last known Network status.
	 * { hostName: string, ipAddress: string, 
		remoteHostStatus: int(0/1/2), internetConnectionStatus: int(0/1/2), localWiFiConnectionStatus: int (0/2) }
     */
	this.lastReachability = null;
};

/**
 * Test the reachibility of the network
 * @param {String} hostname The host name
 * @param {Function} successCallback
 * @param {Object} options (isIpAddress:boolean)
 */
Network.prototype.isReachable = function(hostName, successCallback, options) {
	PhoneGap.exec("Network.isReachable", hostName, GetFunctionName(successCallback), options);
}

/**
 * Called by the geolocation framework when the reachability status has changed.
 * @param {Reachibility} reachability The current reachability status.
 */
Network.prototype.updateReachability = function(reachability) {
    this.lastReachability = reachability;
};

PhoneGap.addConstructor(function() {
    if (typeof navigator.network == "undefined") navigator.network = new Network();
});


/*
 *This class defines APIs for sending log output.
 *@class
 *@constructor
 */
var Logger = function(){
	this.logLevel = Logger.INFO_LEVEL;	
};
// from most verbose, to least verbose
Logger.ALL_LEVEL    = 1; // same as first level
Logger.DEBUG_LEVEL   = 1;
Logger.ERROR_LEVEL   = 2;
Logger.INFO_LEVEL  = 4;
Logger.LOG_LEVEL = 8;
Logger.LOGEVENT_LEVEL = 16;
Logger.OUT_LEVEL = 32;					
Logger.NONE_LEVEL   = 64;

Logger.prototype.setLevel = function(level) {
		this.logLevel = level;
}
/**
 * Utility function for rendering and indenting strings, or serializing
 * objects to a string capable of being printed to the console.
 * @param {Object|String} message The string or object to convert to an indented string
 * @private
 */
Logger.prototype.processMessage = function(message) {
		if (typeof(message) == 'object') {
 
/**
 * @function
 * @ignore
 */
		function indent(str) {
			return str.replace(/^/mg, "    ");
		}
/**
 * @function
 * @ignore
 */
		function makeStructured(obj) {
		var str = "";
        for (var i in obj) {
          try {
            if (typeof(obj[i]) == 'object') {
              str += i + ":\n" + indent(makeStructured(obj[i])) + "\n";
            } else {
              str += i + " = " + indent(String(obj[i])).replace(/^    /, "") + "\n";
		    }
         } catch(e) {
            str += i + " = EXCEPTION: " + e.message + "\n";
		 }
         }
         return str;
         }

    return (""+ "Object:\n" + makeStructured(message));
   }
    else
	return message;
};
													
/*
 *This function is to ouput some warnings
 *@param {String} message the message that you want to output 
 */
Logger.prototype.debug = function(message){
	if (PhoneGap.available && this.logLevel <= Logger.DEBUG_LEVEL)
	PhoneGap.exec('Logger.log',
				  this.processMessage(message),
				  { logLevel: 'DEBUG' }
				  );
	else
	console.log(message);     
};
/*
 *This function is to output error messages
 *@param {String} message the message that you want to output 
 */
Logger.prototype.error = function(message){
	if (PhoneGap.available && this.logLevel <= Logger.ERROR_LEVEL)
	PhoneGap.exec('Logger.log',
				  this.processMessage(message),
				  { logLevel: 'ERROR' }
				  );
	else
	console.log(message);      
};
/*
 *This function is to output some info
 *@param {String} message the message that you want to output 
 */
Logger.prototype.info = function(message){
	if (PhoneGap.available && this.logLevel <= Logger.INFO_LEVEL)
	PhoneGap.exec('Logger.log',
				  this.processMessage(message),
				  { logLevel: 'DEBUG' }
				  );
	else
	console.log(message);      
};
/*
 *This function is to output some info
 *@param {String} message the message that you want to output 
 */
Logger.prototype.log = function(message){
	if (PhoneGap.available && this.logLevel <= Logger.LOG_LEVEL)
	PhoneGap.exec('Logger.log',
				  this.processMessage(message),
				  { logLevel: 'LOG' }
				  );
	else
	console.log(message);     
};
/*
 *This function is to output some info
 *@param {String} message the message that you want to output 
 *@param {Number} level 
 */
Logger.prototype.logEvent = function(message,level){
	if (PhoneGap.available && this.logLevel <= Logger.LOGEVENT_LEVEL)
	PhoneGap.exec('Logger.logEvent',
				  this.processMessage(message),level,
				  { logLevel: 'LOGEVENT' }
				  );
	else
	console.log(message);  
};
/*
 *This function is to output messages
 *@param {String} message the message that you want to output 
 */
Logger.prototype.out = function(message){
	if (PhoneGap.available && this.logLevel <= Logger.OUT_LEVEL)
	PhoneGap.exec('Logger.log',
				  this.processMessage(message),
				  { logLevel: 'OUT' }
				  );
	else
	console.log(message); 
};

PhoneGap.addConstructor(function() {
		if (typeof navigator.logger == "undefined") navigator.logger = new Logger();
});

/**
 * This class provides access to TN API.
 * @constructor
 */
function Maitai(){
}
/**
 * @fileoverview This file is about TN realted APIs in SDK
 * @version 1.0
 */
/**
 * this function is to show Map with address specified by user  on Mobile device.
 * @param {string} developerKey obtained after you register,this key must be URI Encoded
 * @param {string} address destination specified in the form of Full Address,City&State,Zip Code or Airport Code.
 * @param {String} callbackUrl to be used in the response in the following
 *									format, cb=<your_app_id>://<yourapp_entrypoint> This String should be URL encoded
 */
Maitai.prototype.TNMapDisplay1 = function(developerKey,address,callbackUrl){
	PhoneGap.exec("Maitai.TNMapDisplay1", developerKey, address, callbackUrl);
};
/**
 * this function is also to show Map with address specified by user  on Mobile device.
 * @param {Number} latitude specified as floating point number.
 * @param {Number} longitude specified as floating point number.
 * @param {Number} locationErrSize error size of location measurements in meters.
 * @param {String} callbackUrl to be used in the response in the following
 *									format, cb=<your_app_id>://<yourapp_entrypoint> This String should be URL encoded
 */
Maitai.prototype.TNMapDisplay2 = function(latitude,longitude,locationErrSize,callbackUrl){
	PhoneGap.exec("Maitai.TNMapDisplay2", latitude,longitude,locationErrSize,callbackUrl);
};
/**
 * this function deals with voice navigation from current location to specified Address.
 * @param {string} developerKey obtained after you register,this key must be URI Encoded
 * @param {string} address destination specified in the form of Full Address,City&State,Zip Code or Airport Code.
 * @param {String} callbackUrl to be used in the response in the following
 *									format, cb=<your_app_id>://<yourapp_entrypoint> This String should be URL encoded
 * @param {Number} label(optional) to be added to the recent stops list in Telenav application.
 * @param {Number} option(optional) indicating if the user should be prompted before starting the navigation,default value is yes
 */
Maitai.prototype.TNNavTo = function(developerKey,address,callbackUrl,label,option){
	PhoneGap.exec("Maitai.TNNavTo", developerKey,address,callbackUrl,label,option);
};
/**
 * this function is to show turn-by-turn route for a specific origin and destination. If the origin is not specified,
 * then the current location will be used as the origin.
 * @param {string} developerKey obtained after you register,this key must be URI Encoded
 * @param {string} startingAddress(optional) specified in the form of Full Address,City&State,Zip Code or Airport Code.
 * @param {String} destinationAddress specified in the form of full address, city&state, zip code or airport code
 * @param {String} callbackUrl to be used in the response in the following
 *									format, cb=<your_app_id>://<yourapp_entrypoint> This String should be URL encoded
 * @param {Number} destLabel(optional) added to the recent stops list in Telenav application
 */
Maitai.prototype.TNDirection = function(developerKey,startingAddress,destinationAddress,callbackUrl,destLabel){
	PhoneGap.exec("Maitai.TNDirection", developerKey,startingAddress,destinationAddress,callbackUrl,destLabel);
};
/**
 * this function is to search for local point of interest by specified term.
 * @param {string} developerKey obtained after you register,this key must be URI Encoded
 * @param {string} searchTerm keywords specified by users
 * @param {string} address destination specified in the form of Full Address,City&State,Zip Code or Airport Code.
 * @param {String} callbackUrl to be used in the response in the following
 *									format, cb=<your_app_id>://<yourapp_entrypoint> This String should be URL encoded
 */
Maitai.prototype.TNSearch1 = function(developerKey,searchTerm,address,callbackUrl){
	PhoneGap.exec("Maitai.TNSearch1", developerKey,searchTerm,address,callbackUrl);
};
/**
 * this function is to search for local point of interest by specified term.
 * @param {string} developerKey obtained after you register,this key must be URI Encoded
 * @param {string} searchTerm keywords specified by users
 * @param {Number} latitude specified as floating point number.
 * @param {Number} longitude specified as floating point number.
 * @param {Number} locationErrSize error size of location measurements in meters.
 * @param {String} callbackUrl to be used in the response in the following
 *									format, cb=<your_app_id>://<yourapp_entrypoint> This String should be URL encoded
 */
Maitai.prototype.TNSearch2 = function(developerKey,searchTerm,latitude,longtitude,locationErrSize,callbackUrl){
	PhoneGap.exec("Maitai.TNSearch2", developerKey,searchTerm,latitude,longtitude,locationErrSize,callbackUrl);
};
/**
* this function is to sendrequest for remote server.
* @param {string} requestString 
*/
Maitai.prototype.SendRequest = function(requestString){
	PhoneGap.exec("Maitai.TNSendRequest", requestString);
};
PhoneGap.addConstructor(function() {
	if (typeof navigator.maitai == "undefined") navigator.maitai = new Maitai();
});


//***********************************************************************
//Util method
/**
 * If JSON not included, use our own stringify. (Android 1.6)
 * The restriction on ours is that it must be an array of simple types.
 *
 * @param args
 * @return
 */
PhoneGap.stringify = function(args) {
    if (typeof JSON == "undefined") {
        var s = "[";
        for (var i=0; i<args.length; i++) {
            if (i > 0) {
                s = s + ",";
            }
            var type = typeof args[i];
            if ((type == "number") || (type == "boolean")) {
                s = s + args[i];
            }
            else if (args[i] instanceof Array) {
            	s = s + "[" + args[i] + "]";
            }
            else if (args[i] instanceof Object) {
            	var start = true;
            	s = s + '{';
            	for (var name in args[i]) {
            		if (!start) {
            			s = s + ',';
            		}
            		s = s + '"' + name + '":';
            		var nameType = typeof args[i][name];
            		if ((nameType == "number") || (nameType == "boolean")) {
            			s = s + args[i][name];
            		}
            		else {
                        s = s + '"' + args[i][name] + '"';            			
            		}
                    start=false;
            	} 
            	s = s + '}';
            }
            else {
                var a = args[i].replace(/\\/g, '\\\\');
                a = a.replace(/"/g, '\\"');
                s = s + '"' + a + '"';
            }
        }
        s = s + "]";
        return s;
    }
    else {
        return JSON.stringify(args);
    }
};