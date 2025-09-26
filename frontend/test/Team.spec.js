import { InMemoryEntitiesService } from "@/services/in-memory-entities-service";
import { mount } from "@vue/test-utils";
import Team from "@/models/team";
import {reactive} from "vue";
import User from "@/models/user";
import Warehouse from "@/models/warehouse";
import TeamComponent from "@/components/teams/TeamComponent.vue";

let wrapper;
let teamsService;
let usersService;
let warehousesService


beforeEach(async function() {
     teamsService = new InMemoryEntitiesService(10000, (id) => Team.createSampleTeam(id));
     usersService = new InMemoryEntitiesService(100001, (id) => User.createSample(id));
     warehousesService = new InMemoryEntitiesService(100002, (id) => Warehouse.createSampleWarehouse(id));


    wrapper = await mount(TeamComponent, {
        global: {
            provide: {
                "warehousesService": reactive(warehousesService),
                "teamsService": reactive(teamsService),
                "usersService": reactive(usersService),
            },
        },
    });
});
describe('Team', () => {
    it('renders correctly', () => {
        // ASSERT
        expect(wrapper.exists()).toBe(true);
    });

    it('fetches and renders content from the service', async () => {
        // FIRST: Isolated
        // Arrange
        // Ensure that findAll was called
        const teams = teamsService.findAll();

        // Act
        // Wait for the next tick for the component to update
        await wrapper.vm.$nextTick();

        // Find PostComponent
        const teamComponent = await wrapper.findComponent(TeamComponent);
        const teamNames = await teamComponent.findAll('#teamName');

        // Assert
        // Assert that the rendered content matches the fetched data
        expect(teamNames[0].text()).toBe(teams[0].name);
    });

    it('should add new team', async () => {
        // FIRST: Arrange (Setup)
        const searchAddComponent = wrapper.findComponent({ name: "TeamTopComponent" });
        const teamAddComponent = wrapper.findComponent({ name: "TeamAddComponent" });
        const initialTeamsLength = teamsService.findAll().length;

        // Assert before the action
        expect(searchAddComponent.exists()).toBe(true);
        expect(teamAddComponent.exists()).toBe(true);

        const addButton = searchAddComponent.find('#add_icon');
        expect(addButton.exists()).toBe(true);

        // ACT
        // Trigger the click event on the add button
        addButton.trigger('click');

        // Wait for the next tick to allow Vue to update the DOM
        await wrapper.vm.$nextTick();

        // Check if the input element exists
        const inputElement = document.getElementById('swal-input1');
        inputElement.value = "test";

        const dropdownElement = document.getElementById('swal-input4');
        expect(dropdownElement).toBeTruthy();

        const options = dropdownElement.querySelectorAll('option');
        expect(options.length).toBeGreaterThan(0);
        console.log(options);
        options[0].value = "Solar Solution";
        console.log(options[0].value);

        // Mock scrolling function
        window.scrollTo = jest.fn();

        // Confirm the swal (SweetAlert) dialog
        document.querySelector('.swal2-confirm.swal2-styled.swal2-default-outline').click();

        // Wait for multiple ticks to allow Vue to update the DOM
        await new Promise(resolve => setTimeout(resolve, 1)); // Adjust the duration as needed

        // ASSERT
        // Update the teams after the add operation
        const updatedTeams = teamsService.findAll();

        // Assert that the length of teams is one more after the add operation
        expect(updatedTeams.length).toBeGreaterThan(initialTeamsLength);
    });


    it('should delete a team', async function() {
        // FIRST: Arrange (Setup)
        // Find the TeamContainer
        const teamContainer = wrapper.findComponent({ name: "TeamContainer" });

        // Ensure the TeamContainer is found
        expect(teamContainer.exists()).toBe(true);

        // Find the delete button inside teamContainer
        const deleteTeamImg = wrapper.findAll('#delete_team');

        // Get the initial teams length
        const initialTeamsLength = teamsService.findAll().length;

        // Act
        // Trigger the click event on the delete button
        await deleteTeamImg[1].trigger('click');

        // Wait for the next tick to allow Vue to update the DOM
        await wrapper.vm.$nextTick();

        // Mock scrolling function
        window.scrollTo = jest.fn();

        // Manually trigger the click event on the confirm button in the SweetAlert2 dialog
        document.querySelector('.swal2-confirm.swal2-styled.swal2-default-outline').click();
        await wrapper.vm.$nextTick();

        // ASSERT
        // Update the teams after the delete operation
        const updatedTeams = teamsService.findAll();

        // Assert that the length of teams is one less after the delete operation
        expect(updatedTeams.length).toBe(initialTeamsLength - 1);
    });


    it('should cancel delete a team', async function() {
        // FIRST: Arrange (Setup)
        // Find the TeamContainer
        const teamContainer = wrapper.findComponent({ name: "TeamContainer" });

        // Ensure the TeamContainer is found
        expect(teamContainer.exists()).toBe(true);

        // Find the delete button inside teamContainer
        const deleteTeamImg = wrapper.findAll('#delete_team');

        // Get the initial teams length
        const initialTeamsLength = teamsService.findAll().length;

        // Act
        // Trigger the click event on the delete button
        await deleteTeamImg[1].trigger('click');

        // Wait for the next tick to allow Vue to update the DOM
        await wrapper.vm.$nextTick();

        window.scrollTo = jest.fn();

        // Manually trigger the click event on the cancel button in the SweetAlert2 dialog
        document.querySelector('.swal2-cancel.swal2-styled.swal2-default-outline').click();
        await wrapper.vm.$nextTick();

        // ASSERT
        // Update the teams after canceling the delete operation
        const updatedTeams = teamsService.findAll();

        // Assert that the length of teams remains unchanged after canceling the delete operation
        expect(updatedTeams.length).toBe(initialTeamsLength);
    });

// it('constructs a proper teams-service', function() {
//     // ASSERT
//     expect(teamsService.entities.length).toBeGreaterThan(0);
// });
})
