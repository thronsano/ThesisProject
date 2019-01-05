function buttonClicked(value) {
    var offset = getParametersValue('offset');
    if (offset == null)
        offset = value;
    else
        offset = parseInt(offset) + parseInt(value);

    updateParams("home", "offset", offset);
}