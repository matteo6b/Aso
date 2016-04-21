'use strict';

describe('Controller Tests', function() {

    describe('Imagen Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockImagen, MockUser, MockAsociacion, MockEvento;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockImagen = jasmine.createSpy('MockImagen');
            MockUser = jasmine.createSpy('MockUser');
            MockAsociacion = jasmine.createSpy('MockAsociacion');
            MockEvento = jasmine.createSpy('MockEvento');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Imagen': MockImagen,
                'User': MockUser,
                'Asociacion': MockAsociacion,
                'Evento': MockEvento
            };
            createController = function() {
                $injector.get('$controller')("ImagenDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'asoApp:imagenUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
