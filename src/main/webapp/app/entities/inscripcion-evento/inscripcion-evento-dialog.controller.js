(function() {
    'use strict';

    angular
        .module('asoApp')
        .controller('InscripcionEventoDialogController', InscripcionEventoDialogController);

    InscripcionEventoDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'InscripcionEvento', 'Evento', 'User'];

    function InscripcionEventoDialogController ($scope, $stateParams, $uibModalInstance, entity, InscripcionEvento, Evento, User) {
        var vm = this;
        vm.inscripcionEvento = entity;
        vm.eventos = Evento.query();
        vm.users = User.query();
        vm.load = function(id) {
            InscripcionEvento.get({id : id}, function(result) {
                vm.inscripcionEvento = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('asoApp:inscripcionEventoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.inscripcionEvento.id !== null) {
                InscripcionEvento.update(vm.inscripcionEvento, onSaveSuccess, onSaveError);
            } else {
                InscripcionEvento.save(vm.inscripcionEvento, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.fechains = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
