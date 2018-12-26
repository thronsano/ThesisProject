function buttonClicked(value) {
    var offset = getURLParameter('offset');
    if (offset == null)
        offset = value;
    else
        offset = parseInt(offset) + parseInt(value);

    document.location.href = updateURLparameters("home", "offset", offset);
}