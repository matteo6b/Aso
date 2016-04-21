(function() {
    'use strict';

    angular
        .module('asoApp')
        .controller('ImagenDetailController', ImagenDetailController);

    ImagenDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Imagen', 'User', 'Asociacion', 'Evento'];

    function ImagenDetailController($scope, $rootScope, $stateParams, entity, Imagen, User, Asociacion, Evento) {
        var vm = this;
        vm.imagen = entity;
        vm.load = function (id) {
            Imagen.get({id: id}, function(result) {
                vm.imagen = result;
            });
        };
        var unsubscribe = $rootScope.$on('asoApp:imagenUpdate', function(event, result) {
            vm.imagen = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
