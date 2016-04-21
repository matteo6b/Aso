(function() {
    'use strict';

    angular
        .module('asoApp')
        .controller('ImagenDeleteController',ImagenDeleteController);

    ImagenDeleteController.$inject = ['$uibModalInstance', 'entity', 'Imagen'];

    function ImagenDeleteController($uibModalInstance, entity, Imagen) {
        var vm = this;
        vm.imagen = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Imagen.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
