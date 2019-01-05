function getParametersValue(parameterName) {
    var pageURL = window.location.search.substring(1);
    var urlVariables = pageURL.split('&');

    for (var i = 0; i < urlVariables.length; i++) {
        var evaluatedParameter = urlVariables[i].split('=');
        if (evaluatedParameter[0] === parameterName) {
            return evaluatedParameter[1];
        }
    }

    return null;
}

function getUpdatedURLParameters(page, key, value) {
    var pageURL = window.location.search.substring(1);
    var urlVariables = pageURL.length === 0 ? "" : pageURL.split('&&');
    var updatedVariables = "/" + page + "?";
    var variableAlreadyExists = false;

    for (var i = 0; i < urlVariables.length; i++) {
        var evaluatedParameter = urlVariables[i].split('=');

        if (evaluatedParameter[0] === key) {
            evaluatedParameter[1] = value;
            variableAlreadyExists = true;
        }
        updatedVariables += evaluatedParameter[0] + "=" + evaluatedParameter[1];
        if (i + 1 < urlVariables.length) {
            updatedVariables += "&&"
        }
    }

    if (!variableAlreadyExists) {
        if (urlVariables.length > 0) {
            updatedVariables += "&&";
        }
        updatedVariables += key + "=" + value;
    }

    return updatedVariables;
}

function updateParams(page, value, parameterName) {
    document.location.href = getUpdatedURLParameters(page, value, parameterName);
}