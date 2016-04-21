(function() {
    'use strict';

    angular
        .module('asoApp')
        .controller('AsociacionDetailController', AsociacionDetailController);

    AsociacionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Asociacion', 'Imagen', 'Evento', 'InscripcionAso', 'Favorito', 'User'];

    function AsociacionDetailController($scope, $rootScope, $stateParams, entity, Asociacion, Imagen, Evento, InscripcionAso, Favorito, User) {
        var vm = this;
        vm.asociacion = entity;
        vm.load = function (id) {
            Asociacion.get({id: id}, function(result) {
                vm.asociacion = result;
            });
        };
        var unsubscribe = $rootScope.$on('asoApp:asociacionUpdate', function(event, result) {
            vm.asociacion = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
