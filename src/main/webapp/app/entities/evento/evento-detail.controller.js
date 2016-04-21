(function() {
    'use strict';

    angular
        .module('asoApp')
        .controller('EventoDetailController', EventoDetailController);

    EventoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Evento', 'InscripcionEvento', 'Imagen', 'Favorito', 'Asociacion'];

    function EventoDetailController($scope, $rootScope, $stateParams, entity, Evento, InscripcionEvento, Imagen, Favorito, Asociacion) {
        var vm = this;
        vm.evento = entity;
        vm.load = function (id) {
            Evento.get({id: id}, function(result) {
                vm.evento = result;
            });
        };
        var unsubscribe = $rootScope.$on('asoApp:eventoUpdate', function(event, result) {
            vm.evento = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
