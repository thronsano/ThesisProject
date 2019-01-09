function buttonClicked(value, url) {
    var offset = getParametersValue('offset');
    if (offset == null)
        offset = value;
    else
        offset = parseInt(offset) + parseInt(value);

    updateParams(url, "offset", offset);
}