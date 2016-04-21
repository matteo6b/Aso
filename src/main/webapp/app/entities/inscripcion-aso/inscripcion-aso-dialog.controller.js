(function() {
    'use strict';

    angular
        .module('asoApp')
        .controller('InscripcionAsoDialogController', InscripcionAsoDialogController);

    InscripcionAsoDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'InscripcionAso', 'Asociacion', 'User'];

    function InscripcionAsoDialogController ($scope, $stateParams, $uibModalInstance, entity, InscripcionAso, Asociacion, User) {
        var vm = this;
        vm.inscripcionAso = entity;
        vm.asociacions = Asociacion.query();
        vm.users = User.query();
        vm.load = function(id) {
            InscripcionAso.get({id : id}, function(result) {
                vm.inscripcionAso = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('asoApp:inscripcionAsoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.inscripcionAso.id !== null) {
                InscripcionAso.update(vm.inscripcionAso, onSaveSuccess, onSaveError);
            } else {
                InscripcionAso.save(vm.inscripcionAso, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.fechaInscrito = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
