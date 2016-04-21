(function() {
    'use strict';

    angular
        .module('asoApp')
        .controller('FavoritoController', FavoritoController);

    FavoritoController.$inject = ['$scope', '$state', 'Favorito'];

    function FavoritoController ($scope, $state, Favorito) {
        var vm = this;
        vm.favoritos = [];
        vm.loadAll = function() {
            Favorito.query(function(result) {
                vm.favoritos = result;
            });
        };

        vm.loadAll();
        
    }
})();
