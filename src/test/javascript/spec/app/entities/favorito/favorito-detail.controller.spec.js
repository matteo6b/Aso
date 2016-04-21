'use strict';

describe('Controller Tests', function() {

    describe('Favorito Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockFavorito, MockAsociacion, MockUser, MockEvento;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockFavorito = jasmine.createSpy('MockFavorito');
            MockAsociacion = jasmine.createSpy('MockAsociacion');
            MockUser = jasmine.createSpy('MockUser');
            MockEvento = jasmine.createSpy('MockEvento');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Favorito': MockFavorito,
                'Asociacion': MockAsociacion,
                'User': MockUser,
                'Evento': MockEvento
            };
            createController = function() {
                $injector.get('$controller')("FavoritoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'asoApp:favoritoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
