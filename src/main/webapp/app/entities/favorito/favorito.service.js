(function() {
    'use strict';
    angular
        .module('asoApp')
        .factory('Favorito', Favorito);

    Favorito.$inject = ['$resource'];

    function Favorito ($resource) {
        var resourceUrl =  'api/favoritos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
