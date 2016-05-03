/**
 * Created by Mateo on 29/4/16.
 */
(function() {
    'use strict';
    angular
        .module('google.autocomplete', [])
        .directive('gmapAutocomplete', gmapAutocomplete);

    gmapAutocomplete.$inject = ['$q', 'uiGmapGoogleMapApi'];
    /* @ngInject */
    function gmapAutocomplete($q, uiGmapGoogleMapApi) {
        // Usage:
        //  You'll want three attributes on your input element:
        //  gmap-autocomplete: initializes the directive
        //  gmap-autocomplete-callback: takes a function from parent scope, called
        //                              when user selects an address from the dropdown
        //  gmap-autocomplete-model: takes a variable from parent scope, attaches
        //                           the google map API's Autocomplete object
        var directive = {
            link: link,
            scope: {
                gmapAutocompleteCallback: '&?',
                gmapAutocompleteModel: '=?'
            }
        };
        return directive;
        function link(scope, element, attrs) {
            if (element[0].nodeName === 'INPUT') {
                // we don't want input's autocomplete to mess with our dropdown
                attrs.autocomplete = "off";

                generateAutocomplete(element[0], scope.gmapAutocompleteCallback)
                    .then(function(autocomplete) {
                        scope.gmapAutocompleteModel = autocomplete;
                    });
            }
        }

        // Attach the google maps Autocomplete search box to an element,
        // bind a given callback to dropdown selection,
        // return the Autocomplete object
        function generateAutocomplete(element, changeCallback) {
            // make google.maps object available
            return uiGmapGoogleMapApi
                .then(function(maps) {
                    // promise-ify the navigator.geolocation request
                    return $q(function(resolve, reject) {
                        var autocomplete = new maps.places.Autocomplete(element, {types: ['geocode']});
                        autocomplete.addListener('place_changed', changeCallback);

                        // ask for user's position so we can bias gmap autocomplete
                        // TODO: check for existence of window.navigator? old browsers exist...
                        navigator.geolocation.getCurrentPosition(function(position) {
                            var bounds = new maps.Circle({
                                lat: position.coords.latitude,
                                lng: position.coords.longitude
                            });
                            autocomplete.setBounds(bounds.getBounds());

                            resolve(autocomplete);
                        }, function() {
                            // couldn't get position so we'll create an autocomplete without the bias
                            resolve(autocomplete);
                        });
                    });
                });
        }
    }
})();
