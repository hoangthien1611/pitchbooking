(function ($) {
    //var domain = 'http://localhost:55390/';
    var domain  ='https://www.sport9.vn/';

    // insert to center
    $.getJSON(domain + 'ads/products/87/15/?thumbsize=250', function (result) {
        var slideShowId = 's9adslidehow-center';
        var html = buildAdHtml(result, slideShowId);
        var firstCenterDiv = $('.p-content ul.list-item-san')[0];
        $(html).insertAfter($(firstCenterDiv));

        setTimeout(function () {
            setupSlideShow(slideShowId, 5);
        }, 1000);
    });

    // insert to right
    $.getJSON(domain + 'ads/products/87/5/?thumbsize=350', function (result) {
        var slideShowId = 's9adslidehow-right';
        var html = buildAdHtml(result, slideShowId);
        var rightDiv = $('.sidebar-tien-san')[0];
        if (!rightDiv) {
            rightDiv = $('.ads-banner-right')[0];
        }
        $(html).insertAfter(rightDiv);

        setTimeout(function () {
            setupSlideShow(slideShowId, 1);
        }, 1000);
    });

    function setupSlideShow(id, numOfItems) {
        jQuery('#' + id).owlCarousel({

            navigation: false, // Show next and prev buttons
            loop: true,
            callbacks: true,
            stagePadding: 30,
            margin: 10,

            itemsDesktop: [1199, 1],
            itemsDesktopSmall: [979, 1],
            itemsTablet: [768, 1],
            items: numOfItems,

            //Basic Speeds
            slideSpeed: 100,
            paginationSpeed: 200,
            rewindSpeed: 10,

            //Autoplay
            autoPlay: true,
            stopOnHover: true,
            pagination: true,
            autoHeight: true

        });
    }

    function buildAdHtml(result, slideshowId, showName) {
        var html = '<div class="s9ad-slider owl-carousel" id="' + slideshowId + '" style="max-height:500px;overflow:hidden">';
        $.each(result, function (index, item) {
            var priceDiv = '<div class="ad-pricediv">';
            priceDiv += '<a href="' + domain + item.SeName + '" title="' + item.Name + '" target="_blank">';
            priceDiv += '<span class="ad-pricevalue">' + item.ProductPrice.Price + '</span>';
            if (item.ProductPrice.OldPrice) {
                priceDiv += '&nbsp;&nbsp;<span class="ad-priceOld">' + item.ProductPrice.OldPrice + '</span>';
            }
            priceDiv += '</a>'
            priceDiv += '</div>';

            var htmlItem = ['<div class="ad-item">',
                '<a href="' + domain + item.SeName + '" title="' + item.Name + '" target="_blank">',
                '<img src="' + item.DefaultPictureModel.ImageUrl + '" alt="' + item.Name + '" style="width:100%"/>',
                '</a>',
                '<h4>',
                '<a href="' + domain + item.SeName + '" title="' + item.SeName + '" target="_blank">' + item.Name + '</a>',
                '</h4>',
                priceDiv,
                '</div>'].join('\n');

            html += htmlItem;
        });
        html += '</div>';

        return html;
    }
})(jQuery);
