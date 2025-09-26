import {InMemoryEntitiesService} from "@/services/in-memory-entities-service";
import User from "@/models/user";

let UsersService;

beforeEach(function (){
    UsersService = new InMemoryEntitiesService(30000, (id) => User.createSample(id))
});
describe('UsersService', () => {
    it('constructs a proper users-service', function() {
        expect(UsersService.entities.length)
            .toBeGreaterThan(0);
    })

    it('findAll returns all', function() {
        let users = UsersService.findAll();
        expect(users).toStrictEqual(UsersService.entities);
    })


    it('deleteById deletes the specified book', function() {
        const user = UsersService.entities[0];

        UsersService.deleteById(user.id);

        expect(UsersService.findById(user.id)).toBeUndefined();
    })

})


