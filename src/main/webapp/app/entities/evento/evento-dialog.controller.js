(function() {
    'use strict';

    angular
        .module('asoApp')
        .controller('EventoDialogController', EventoDialogController);

    EventoDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Evento', 'InscripcionEvento', 'Imagen', 'Favorito', 'Asociacion'];

    function EventoDialogController ($scope, $stateParams, $uibModalInstance, entity, Evento, InscripcionEvento, Imagen, Favorito, Asociacion) {
        var vm = this;
        vm.evento = entity;
        vm.inscripcioneventos = InscripcionEvento.query();
        vm.imagens = Imagen.query();
        vm.favoritos = Favorito.query();
        vm.asociacions = Asociacion.query();
        vm.load = function(id) {
            Evento.get({id : id}, function(result) {
                vm.evento = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('asoApp:eventoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.evento.id !== null) {
                Evento.update(vm.evento, onSaveSuccess, onSaveError);
            } else {
                Evento.save(vm.evento, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.fecha = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
