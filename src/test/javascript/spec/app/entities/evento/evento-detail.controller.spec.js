'use strict';

describe('Controller Tests', function() {

    describe('Evento Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockEvento, MockInscripcionEvento, MockImagen, MockType, MockFavorito, MockAsociacion;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockEvento = jasmine.createSpy('MockEvento');
            MockInscripcionEvento = jasmine.createSpy('MockInscripcionEvento');
            MockImagen = jasmine.createSpy('MockImagen');
            MockType = jasmine.createSpy('MockType');
            MockFavorito = jasmine.createSpy('MockFavorito');
            MockAsociacion = jasmine.createSpy('MockAsociacion');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Evento': MockEvento,
                'InscripcionEvento': MockInscripcionEvento,
                'Imagen': MockImagen,
                'Type': MockType,
                'Favorito': MockFavorito,
                'Asociacion': MockAsociacion
            };
            createController = function() {
                $injector.get('$controller')("EventoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'asoApp:eventoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
