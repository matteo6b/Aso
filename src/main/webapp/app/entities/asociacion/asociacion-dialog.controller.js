(function() {
    'use strict';

    angular
        .module('asoApp')

        .controller('AsociacionDialogController', AsociacionDialogController);

    AsociacionDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Asociacion', 'Imagen', 'Evento', 'InscripcionAso', 'Favorito','Type', 'User','NgMap'];






    function AsociacionDialogController ($scope, $stateParams, $uibModalInstance,entity, Asociacion, Imagen, Evento, InscripcionAso, Favorito,Type, User,NgMap) {
        var vm = this;
        vm.asociacion = entity;
        vm.imagens = Imagen.query();
        vm.eventos = Evento.query();
        vm.inscripcionasos = InscripcionAso.query();
        vm.favoritos = Favorito.query();
        vm.users = User.query();
        vm.types= Type.query;
        var map;




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
            vm.asociacion.lat = vm.lat;
            vm.asociacion.lng = vm.lng;
            vm.asociacion.streetAdress=vm.streetAdress;


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




        if (vm.asociacion.id !== null) {

            NgMap.getMap().then(function (evmap) {
                vm.address=vm.asociacion.streetAdress;
                map = evmap;
            });
        }
        else {

            NgMap.getMap().then(function (evmap) {
                map = evmap;
            });
        }
        vm.lat = [];
        vm.lng = [];
         vm.streetAdress;
        vm.placeChanged = function() {
            vm.place = this.getPlace();
             map.setCenter(vm.place.geometry.location);
            vm.lat = vm.place.geometry.location.lat();
            vm.lng = vm.place.geometry.location.lng();
            vm.streetAdress=vm.place.formatted_address;

        }

    }
})();
