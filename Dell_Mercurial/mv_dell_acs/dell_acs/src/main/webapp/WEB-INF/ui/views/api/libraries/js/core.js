//
// @author Navin Raj.
//
// this script will require a global javascript variable called CONTENT_SERVER_CONFIGURATION
//
// var CONTENT_SERVER_CONFIG = {
//     SERVER_PATH: '',
//     API_KEY: ''
// };
//

// namespace for ContentServer specific classes/methods.
var cs = {};

// execution namespace
cs.api = {};


// logging
cs.log = {};
cs.log.level = 0; // the higher the lesser the logs we see. 0 is ALL, 10 will be none.
cs.log.levelLabels = ["DEBUG", "INFO", "WARN", "ERROR"];
cs.log.functionNames = ["debug", "info", "warn", "error"];
cs.log.availableLoggers = [window.console, console];
cs.log.__log = function (level, args) {
    var message = args[0];
    if (level < cs.log.level) {
        return;
    }
    var isLogAvailable = window.console != null || (console != null && console.log != null);
    if (isLogAvailable) {
        var d = new Date();
        var timeStr = d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds() + "." + d.getMilliseconds();
        message = "[" + cs.log.levelLabels[level] + "] " + timeStr + " - " + message;
    }


    try {
        for (var k in cs.log.availableLoggers) {
            var obj = cs.log.availableLoggers[k];
            if (obj != null && obj[cs.log.functionNames[level]] != null) {
                cs.log.availableLoggers[k][cs.log.functionNames[level]].apply(cs.log.availableLoggers[k], args);
                break;
            }
        }
    } catch (e) {
        // ignore
        if (window.console != null && window.console.log != null) {
            window.console.log(message);
        } else if (console != null && console.log != null) {
            console.log(message);
        }
    }
};

cs.log.debug = function () {
    cs.log.__log(0, arguments);
};
cs.log.info = function () {
    cs.log.__log(1, arguments);
};
cs.log.warn = function () {
    cs.log.__log(2, arguments);
};
cs.log.error = function () {
    cs.log.__log(3, arguments);
};

cs.assert = {};
cs.assert.notNull = function (expression, message) {
    if (expression == null || expression == undefined) {
        if (message == null || message == undefined) {
            message = "[Assertion Error] - expression is null";
        }
        throw new Error(message);
    }
};
cs.assert.notNullParameter = function (expression, paramName) {
    cs.assert.notNull(expression, "Parameter '" + paramName + "' cannot be null.");
};

cs.assert.isTrue = function (expression, message) {
    if (expression == null || expression == undefined || expression != true) {
        if (message == null || message == undefined) {
            message = "[Assertion Error] - expression is not true";
        }
        throw new Error(message);
    }
};

// Profiler
cs.profiler = {};
cs.profiler.enabled = true; // defaults to false in production.
cs.profiler.events = [];
cs.profiler.Event = function (id) {
    this.id = id;
    this.startTime = 0;
    this.endTime = 0;
    this.elapsed = 0;
    this.index = 0;
};
cs.profiler.Event.prototype.start = function () {
    if (cs.profiler.enabled) {
        this.startTime = new Date().getTime();
        cs.log.debug("profiler event '" + this.id + "' index:='"+this.index
            + "' started at " + this.startTime);
    }
};
cs.profiler.Event.prototype.end = function () {
    if (cs.profiler.enabled) {
        this.endTime = new Date().getTime();
        this.elapsed = this.endTime - this.startTime;
        cs.log.debug("profiler event '" + this.id + "' index:='"+this.index
            +"' ended at " + this.endTime + " with a elapsedTime:=" + this.elapsed);
    }
};

cs.profiler.start = function (id) {
    if (cs.profiler.enabled) {
        if (cs.profiler.events[id] == null) {
            cs.profiler.events[id] = {
                id:id,
                averageTime:0,
                instances:[],
                shortestEvent:0,
                longestEvent:0
            };
        }
        var event = new cs.profiler.Event(id);
        event.index = cs.profiler.events[id].instances.push(event) - 1;// the current index of the element.
        event.start();
        return event;
    } else {
        return new cs.profiler.Event(id);
    }
};
cs.profiler.formatter={spacer:"                                                                                      "};
cs.profiler.statistics = function (filterId, displayFilterId) {
    if (cs.profiler.enabled) {
        for (var id in cs.profiler.events) {
            if(filterId != null && filterId != id){
                continue; // ignore if filterID was passed.
            }
            var events = cs.profiler.events[id];
            var totalElapsed = 0;
            var totalExecuted = 0;
            for (var i = 0; i < events.instances.length; i++) {
                var event = events.instances[i];
                if (event.elapsed > 0) {
                    totalElapsed = totalElapsed + event.elapsed;
                    totalExecuted++;
                    if (event.elapsed > events.longestEvent || events.longestEvent == 0) {
                        events.longestEvent = event.elapsed;
                    }
                    if (event.elapsed < events.shortestEvent || events.shortestEvent == 0) {
                        events.shortestEvent = event.elapsed;
                    }
                }
            }
            if (totalElapsed == 0 || totalExecuted == 0) {
                // may not have any events, or its still running, so ignore.
            } else {
                events.averageTime = totalElapsed / totalExecuted;
            }
        }

        // computation done.
        // just log it.
        var msg = "===========================================\n------ profiler stats ------\n";
        for (var id in cs.profiler.events) {
            if(filterId != null && filterId != id){
                continue; // ignore if filterID was passed.
            }
            var events = cs.profiler.events[id];
            if(displayFilterId == null || displayFilterId == true ){
                var eventId = events.id;
                if(eventId.length<60){
                    eventId = eventId + cs.profiler.formatter.spacer.substr(0, 60-eventId.length);
                }
                msg = msg + "id:=" + eventId +"";
            }
            msg = msg + " \taverageTime:=" + Math.round(events.averageTime*100)/100 + " \tshortestEvent:="
                + events.shortestEvent + " \tlongestEvent:=" + events.longestEvent+"\n";
        }
        msg = msg + "\n===========================================";
        cs.log.info(msg);
    } else {
        cs.log.info("Profiler is not enabled.");
    }
};


// API base implementation.
//
// @author Navin Raj.
//
// this script will require a global javascript variable called CONTENT_SERVER_CONFIGURATION
//
// var CONTENT_SERVER_CONFIG = {
//     SERVER_PATH: '',
//     API_KEY: ''
// };
//

// internal helper functions
cs.api.__internal = {};

// API registry
cs.api.base = {};
cs.api.config = {};
cs.api.config.enableCaching = true; // if its set to false, an additional argument will be passed to Ajax as a random.
cs.api.config.useJSONP = false;
cs.api.base.APIService = function (serviceName, version) {
    cs.assert.notNullParameter(serviceName, "serviceName");
    cs.assert.notNullParameter(version, "version");
    this.serviceName = serviceName;
    this.version = version;
    this.methods = [];
};
cs.api.base.APIMethod = function (apiService, methodName, endPoint) {
    cs.assert.notNullParameter(apiService, "apiService");
    cs.assert.notNullParameter(methodName, "methodName");
    cs.assert.notNullParameter(endPoint, "endPoint");
    this.apiService = apiService;
    this.methodName = methodName;
    this.endPoint = endPoint;
    this.parameters = [];
};

cs.api.base.APIMethod.prototype.registerAPIParameter = function (paramName, paramType, required, defaultValue) {
    cs.assert.notNullParameter(paramName, "paramName");
    cs.assert.notNullParameter(paramType, "paramType");
    cs.assert.notNullParameter(required, "required");
    cs.assert.notNullParameter(defaultValue, "defaultValue");
    this.parameters.push(new cs.api.base.APIParameter(this, paramName, paramType, required, defaultValue));
};
cs.api.base.APIMethod.prototype.toString = function () {
    var str = this.toSimpleString() + "( ";
    for (var i = 0; i < this.parameters.length; i++) {
        var param = this.parameters[i];
        str = str + param.toString();
        if (i + 1 != this.parameters.length) {
            str = str + ",";
        }
    }
    str = str + ");";
    return str;
};
cs.api.base.APIMethod.prototype.toSimpleString = function () {
    return this.apiService.serviceName + "." + this.methodName;
}

cs.api.base.APIMethod.prototype.execute = function () {
    var callback = null;
    var paramValues = [];
    if (arguments == null || arguments == undefined) {
        // no arguments found.
    } else {
        callback = arguments[arguments.length - 1];
        if (!(callback instanceof Function || typeof this === 'function')) {
            callback = null;
            for (var i = 0; i < arguments.length; i++) {
                paramValues[i] = arguments[i];
            }
        } else {
            // the last argument seems to be a callback.
            for (var i = 0; i < arguments.length - 1; i++) {
                paramValues[i] = arguments[i];
            }
        }

        // if its not a function, just set it to null.
    }
    var executionParameters = [];
    // now check if the APIMethod parameters are valid.
    if (this.parameters.length > 0) {
        // we require parameters for execution.
        for (var i = 0; i < this.parameters.length; i++) {
            var inputValue = null;
            if (i < paramValues.length) {
                inputValue = paramValues[i];
            }
            executionParameters[i] = this.parameters[i].getValue(inputValue);
        }
    }
    var executeMethod = null;
    try {
        executeMethod = cs.api[this.apiService.version].__internal.executeMethod;
    } catch (e) {
    }

    if (executeMethod == null || executeMethod == undefined) {
        throw new Error("The API Executor callback has not been defined properly. " +
            "We require cs.api." + this.apiService.version + ".__internal.executeMethod to exist as a function.");
    }
    executeMethod.apply(cs.api[this.apiService.version].__internal, [this, executionParameters, callback]);

};
cs.api.__internal.jsonp = function(apiMethod, executionParameters, methodUrl, paramString, callback){

    cs.assert.notNullParameter(methodUrl,"methodUrl");
    cs.assert.notNullParameter(executionParameters,"executionParameters");
    cs.assert.notNullParameter(methodUrl,"methodUrl");
    cs.assert.notNullParameter(paramString,"paramString");

    var profilerEvent = cs.profiler.start(apiMethod.toSimpleString());

    var callbackMethodName = "__cs_jsonp_" + new Date().getTime();
    window[callbackMethodName] = function(jsonObj) {
        // we got the data.
        // unset this method
        profilerEvent.end();
        try{
            window[callbackMethodName] = null;
            delete window[callbackMethodName];
        } catch(e) {
            // ignore
        }
        var resultObject = null;
        if(jsonObj == null){
            resultObject = {success:false, data:{}, error:"null was received from JSONP callback for apiMethod:="+apiMethod.toSimpleString()};
        } else{
            resultObject = jsonObj;
        }
        if (callback != null) {
            cs.log.debug("executed call " + apiMethod.toSimpleString(), resultObject);
            if(resultObject.hasOwnProperty("data")){
                callback.apply(apiMethod, [resultObject["data"], resultObject]);
            } else {
                callback.apply(apiMethod, [resultObject]);
            }
        } else {
            cs.log.debug("executed call " + apiMethod.toSimpleString() + " with a null callback");
        }
    };

    // append jsonpCallback.
    if(paramString == ""){
        paramString = "?" + "jsonp=" + callbackMethodName;
    }else{
        paramString = paramString + "&" + "jsonp=" + callbackMethodName;
    }
    if(paramString.substr(0,1) != "?"){
        paramString = "?" + paramString;
    }

    var script = document.createElement("script");
    script.src = methodUrl + paramString;
    document.getElementsByTagName('head')[0].appendChild(script);

};

cs.api.__internal.ajax = function (apiMethod, executionParameters, requestHeaders, callback) {

    var paramString = "";
    for (var i = 0; i < apiMethod.parameters.length; i++) {
        paramString = paramString + apiMethod.parameters[i].paramName + "=" + executionParameters[i];
        if (i < apiMethod.parameters.length-1) {
            paramString = paramString + "&";
        }
    }
    if(cs.api.config.enableCaching == false){
        if(paramString.length == 0){
            paramString = "?t="+new Date().getTime();
        } else{
            paramString = paramString + "&t="+new Date().getTime();
        }
    }

    if (CONTENT_SERVER_CONFIG == undefined) {
        throw new Error("CONTENT_SERVER_CONFIG was not defined. Please check the API documents to " +
            "define the API configuration before calling any methods.");
    }
    var methodUrl = CONTENT_SERVER_CONFIG.SERVER_PATH + apiMethod.endPoint;

    if(cs.api.config.useJSONP == true){
        cs.api.__internal.jsonp(apiMethod, executionParameters, methodUrl, paramString, callback);
        return;
    }


    var profilerEvent = cs.profiler.start(apiMethod.toSimpleString());
    var executor = null;
    if (window.XMLHttpRequest) {
        executor = new XMLHttpRequest();
    } else if (window.ActiveXObject) { // InternetExplorer uses ActiveX
        try {
            executor = new ActiveXObject("Msxml2.XMLHTTP")
        } catch (e) {
            try {
                executor = new ActiveXObject("Microsoft.XMLHTTP");
            } catch (E) {
                executor = null;
            }
        }
    }
    if (executor == null || executor == false) {
        throw new Error("The browser does not support ajax requests.");
    }

    executor.onreadystatechange = function () {
        if (executor && executor.readyState == 4) {
            var resultObject = null;
            profilerEvent.end();
            if (executor.status == 200) {
                if (callback != null) {
                    try {
                        resultObject = eval("(" + executor.responseText + ")");
                    } catch (e) {
                        // unable to eval JSON?
                        resultObject = {success:false, data:{}, error:"failed to parse response JSON." + e, responseText:executor.responseText};
                    }
                }
            } else {
                resultObject = {success:false, data:{}, error:"HTTP failure status" + executor.status, responseText:executor.responseText};
            }
            if (callback != null) {
                cs.log.debug("executed call " + apiMethod.toSimpleString(), resultObject);
                callback.apply(apiMethod, [resultObject.data, resultObject]);
            } else {
                cs.log.debug("executed call " + apiMethod.toSimpleString() + " with a null callback");
            }
        }
    };

    executor.open("POST", methodUrl, true);
    executor.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    if (requestHeaders != null) {
        if (typeof requestHeaders == 'array') {
            for (var k in requestHeaders) {
                executor.setRequestHeader(k, requestHeaders[k]);
            }
        } else {
            cs.log.warn("requestHeaders must be an array for " + apiMethod.toString());
        }
    }
    if (paramString == "") {
        executor.send(null);
    } else {
        executor.send(paramString);
    }
};

cs.api.base.APIParameter = function (apiMethod, paramName, paramType, required, defaultValue) {
    this.apiMethod = apiMethod;
    this.paramName = paramName;
    this.paramType = paramType.toLowerCase();
    this.required = required;
    this.defaultValue = defaultValue;
};
cs.api.base.APIParameter.prototype.toString = function () {
    return this.paramType + " " + this.paramName + "[required=" + this.required + ", defaultValue=" + this.defaultValue + "] ";
};

cs.api.base.APIParameter.prototype.getValue = function (srcValue) {
    if (this.required) {
        if (srcValue == null || srcValue == undefined) {
            throw new Error("parameter '" + this.paramName + "' is required for " + this.apiMethod.toString());
        }
    }
    if (srcValue == null) {
        cs.log.debug(" parameter "+ this.paramName + " was null for request:="+this.apiMethod.toSimpleString());
        srcValue = this.defaultValue;
    }
    if (this.paramType == "long" || this.paramType == "int") {
        if (typeof srcValue == "number") {
            return parseInt(srcValue);
        } else {
            throw new Error("parameter '" + this.paramName + "' must be a int/long for " + this.apiMethod.toString()
                + " but got " + srcValue + " of type:=" + (typeof srcValue));
        }
    } else if (this.paramType == "float" || this.paramType == "decimal") {
        if (typeof srcValue == "number") {
            return parseFloat(srcValue);
        } else {
            throw new Error("parameter '" + this.paramName + "' must be a float/decimal for " + this.apiMethod.toString()
                + " but got " + srcValue + " of type:=" + (typeof srcValue));
        }
    } else if (this.paramType == "string") {
        if (srcValue instanceof String || typeof srcValue == "string") {
            return srcValue.valueOf();
        }
        return new String(srcValue).valueOf();
    } else {
        throw new Error("Unsupported dataType")
    }
};

cs.api.base.APIService.prototype.registerAPIMethod = function (methodName, endPoint) {
    var method = new cs.api.base.APIMethod(this, methodName, endPoint);
    // allows direct invocation of the method.
    this.methods[methodName] = method;

    this[methodName] = function () {
        method.execute.apply(method, arguments);
    };
    return method;
};

cs.api.tests = {};
cs.api.tests.testExecutionCounter = 0;
cs.api.tests.ConcurrentExecutor = function (method, totalCount, args, concurrent) {
    cs.api.tests.testExecutionCounter++;
    this.id = "test-execution-"+cs.api.tests.testExecutionCounter;
    this.method = method;
    cs.api.config.enableCaching = false;
    this.totalCount = totalCount;
    this.args = args;
    this.concurrent = concurrent;
    this.totalFailure = 0;
    this.executionComplete = false;
};

cs.api.tests.ConcurrentExecutor.prototype.start = function () {
    this.completedCount = 0;
    this.currentExecutions = 0;
    this.triggerNext();
};
cs.api.tests.ConcurrentExecutor.prototype.triggerNext = function () {
    if (this.completedCount >= this.totalCount) {
        if(this.executionComplete == false){
            this.executionComplete = true;
            if(cs.profiler.enabled){
                cs.profiler.statistics(this.id, false);
            }
            if(this.totalFailure != 0){
                cs.log.warn(this.id+" had "+this.totalFailure+" failures.");
            }
            if(this.onComplete != undefined &&  typeof this.onComplete == "function"){
                this.onComplete.apply(this,[this]);
            }
        }
        return;
    }
    if (this.currentExecutions < this.concurrent) {
        // submit task.
        var triggerProfiler = cs.profiler.start(this.id);
        var triggerArgs = [];
        // copy fresh arguments each time to handle callbacks.
        for (var i = 0; i < this.args; i++) {
            triggerArgs[i] = this.args[i];
        }
        var self = this;
        triggerArgs.push(function (data) {
            self.completedCount++;
            self.currentExecutions--;
            if(data != null ){
                if(data.hasOwnProperty("status") && data["status"] == false){
                    cs.log.debug("responseJson.status = false");
                    self.totalFailure++;
                }
            } else{
                cs.log.debug("response data was null");
                self.totalFailure++;
            }
            triggerProfiler.end();
            self.triggerNext();
        });
        this.currentExecutions++;
        this.method.apply(this.method, triggerArgs);
        // trigger the next event till maximum concurrent executions is full.
        this.triggerNext();
    }
};

cs.api.tests.execute = function (qualifiedMethodName, args, totalCount, concurrent, onComplete) {
    cs.assert.notNull(qualifiedMethodName, "qualifiedMethodName parameter cannot be null." +
        " Please use a fully qualified name like 'cs.api.v1.CampaignService.getCampaign'");
    if (totalCount == null || totalCount == undefined) {
        totalCount = 1;
    }
    if (concurrent == null || concurrent == undefined) {
        concurrent = 1;
    }
    cs.assert.notNullParameter(arguments, "arguments");
    if (!args.hasOwnProperty("length")) {
        throw new Error("args must be an Array");
    }
    var methodToInvoke = null;
    if (typeof qualifiedMethodName == "function") {
        methodToInvoke = qualifiedMethodName;
    } else {
        try {
            methodToInvoke = eval(qualifiedMethodName);
        } catch (e) {
            throw new Error("Unable to find API Method :=" + qualifiedMethodName);
        }
    }


    var executor = new cs.api.tests.ConcurrentExecutor(methodToInvoke, totalCount, args, concurrent);
    executor.onComplete = onComplete;
    executor.start();

};








