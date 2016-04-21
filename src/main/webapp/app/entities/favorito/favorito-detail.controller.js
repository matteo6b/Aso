(function() {
    'use strict';

    angular
        .module('asoApp')
        .controller('FavoritoDetailController', FavoritoDetailController);

    FavoritoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Favorito', 'Asociacion', 'User', 'Evento'];

    function FavoritoDetailController($scope, $rootScope, $stateParams, entity, Favorito, Asociacion, User, Evento) {
        var vm = this;
        vm.favorito = entity;
        vm.load = function (id) {
            Favorito.get({id: id}, function(result) {
                vm.favorito = result;
            });
        };
        var unsubscribe = $rootScope.$on('asoApp:favoritoUpdate', function(event, result) {
            vm.favorito = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
