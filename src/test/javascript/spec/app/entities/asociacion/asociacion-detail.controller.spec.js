'use strict';

describe('Controller Tests', function() {

    describe('Asociacion Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockAsociacion, MockImagen, MockEvento, MockInscripcionAso, MockFavorito, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockAsociacion = jasmine.createSpy('MockAsociacion');
            MockImagen = jasmine.createSpy('MockImagen');
            MockEvento = jasmine.createSpy('MockEvento');
            MockInscripcionAso = jasmine.createSpy('MockInscripcionAso');
            MockFavorito = jasmine.createSpy('MockFavorito');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Asociacion': MockAsociacion,
                'Imagen': MockImagen,
                'Evento': MockEvento,
                'InscripcionAso': MockInscripcionAso,
                'Favorito': MockFavorito,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("AsociacionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'asoApp:asociacionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
