function buttonClicked(value) {
    var offset = getURLParameter('offset');
    if (offset == null)
        offset = value;
    else
        offset = parseInt(offset) + parseInt(value);

    document.location.href = updateURLparameters("home", "offset", offset);
}

function getURLParameter(sParam) {
    var sPageURL = window.location.search.substring(1);
    var sURLVariables = sPageURL.split('&');
    for (var i = 0; i < sURLVariables.length; i++) {
        var sParameterName = sURLVariables[i].split('=');
        if (sParameterName[0] == sParam) {
            return sParameterName[1];
        }
    }
    return null;
}

function updateURLparameters(page, key, value) {
    var sPageURL = window.location.search.substring(1);
    var sURLVariables = sPageURL.length === 0 ? "" : sPageURL.split('&&');
    var updatedVariables = "/" + page + "?";
    var variableAlreadyExists = false;

    for (var i = 0; i < sURLVariables.length; i++) {
        var sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] == key) {
            sParameterName[1] = value;
            variableAlreadyExists = true;
        }
        updatedVariables += sParameterName[0] + "=" + sParameterName[1];
        if (i + 1 < sURLVariables.length)
            updatedVariables += "&&"
    }

    if (!variableAlreadyExists) {
        if(sURLVariables.length > 0)
            updatedVariables += "&&"
        updatedVariables += key + "=" + value;
    }

    return updatedVariables;
}