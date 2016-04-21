(function() {
    'use strict';

    angular
        .module('asoApp')
        .controller('AsociacionDialogController', AsociacionDialogController);

    AsociacionDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Asociacion', 'Imagen', 'Evento', 'InscripcionAso', 'Favorito', 'User'];

    function AsociacionDialogController ($scope, $stateParams, $uibModalInstance, entity, Asociacion, Imagen, Evento, InscripcionAso, Favorito, User) {
        var vm = this;
        vm.asociacion = entity;
        vm.imagens = Imagen.query();
        vm.eventos = Evento.query();
        vm.inscripcionasos = InscripcionAso.query();
        vm.favoritos = Favorito.query();
        vm.users = User.query();
        vm.load = function(id) {
            Asociacion.get({id : id}, function(result) {
                vm.asociacion = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('asoApp:asociacionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.asociacion.id !== null) {
                Asociacion.update(vm.asociacion, onSaveSuccess, onSaveError);
            } else {
                Asociacion.save(vm.asociacion, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.fecchaCreacion = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
