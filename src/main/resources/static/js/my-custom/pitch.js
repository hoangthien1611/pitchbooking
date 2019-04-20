$(document).ready(function() {
    var latlng = new google.maps.LatLng(16.0607, 108.2167);
    var map = new google.maps.Map(document.getElementById('map'), {
        center: latlng,
        zoom: 12,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    });
    var marker = new google.maps.Marker({
        position: latlng,
        map: map,
        title: 'Set lat/lon values for this property',
        draggable: true
    });
    google.maps.event.addListener(marker, 'dragend', function(a) {
        console.log(a);
        $('.lat').html(a.latLng.lat().toFixed(4));
        $('.long').html(a.latLng.lng().toFixed(4));
    });

    // right click event
    google.maps.event.addListener(map, 'rightclick', function(e) {
        console.log(e);
        var positionDoubleclick = e.latLng;
        marker.setPosition(positionDoubleclick);
        $('.lat').html(e.latLng.lat().toFixed(4));
        $('.long').html(e.latLng.lng().toFixed(4));
    });

    $(document).on("click", ".btn-choose-map", function () {
        var long = $(".long").text();
        var lat = $(".lat").text();

        $("#longitude").val(long);
        $("#latitude").val(lat);
    });
});

// This example adds a search box to a map, using the Google Place Autocomplete
// feature. People can enter geographical searches. The search box will return a
// pick list containing a mix of places and predicted search terms.

// This example requires the Places library. Include the libraries=places
// parameter when you first load the API. For example:
// <script src="https://maps.googleapis.com/maps/api/js?key=YOUR_API_KEY&libraries=places">

// function initAutocomplete() {
//     var map = new google.maps.Map(document.getElementById('map'), {
//         center: {lat: 16.0607, lng: 108.2167},
//         zoom: 13,
//         mapTypeId: 'roadmap'
//     });
//
//     // Create the search box and link it to the UI element.
//     var input = document.getElementById('pac-input');
//     var searchBox = new google.maps.places.SearchBox(input);
//     map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);
//
//     // Bias the SearchBox results towards current map's viewport.
//     map.addListener('bounds_changed', function() {
//         searchBox.setBounds(map.getBounds());
//     });
//
//     var markers = [];
//     // Listen for the event fired when the user selects a prediction and retrieve
//     // more details for that place.
//     searchBox.addListener('places_changed', function() {
//         var places = searchBox.getPlaces();
//
//         if (places.length == 0) {
//             return;
//         }
//
//         // Clear out the old markers.
//         markers.forEach(function(marker) {
//             marker.setMap(null);
//         });
//         markers = [];
//
//         // For each place, get the icon, name and location.
//         var bounds = new google.maps.LatLngBounds();
//         places.forEach(function(place) {
//             if (!place.geometry) {
//                 console.log("Returned place contains no geometry");
//                 return;
//             }
//             var icon = {
//                 url: place.icon,
//                 size: new google.maps.Size(71, 71),
//                 origin: new google.maps.Point(0, 0),
//                 anchor: new google.maps.Point(17, 34),
//                 scaledSize: new google.maps.Size(25, 25)
//             };
//
//             // Create a marker for each place.
//             markers.push(new google.maps.Marker({
//                 map: map,
//                 icon: icon,
//                 title: place.name,
//                 position: place.geometry.location
//             }));
//
//             if (place.geometry.viewport) {
//                 // Only geocodes have viewport.
//                 bounds.union(place.geometry.viewport);
//             } else {
//                 bounds.extend(place.geometry.location);
//             }
//         });
//         map.fitBounds(bounds);
//     });
// }