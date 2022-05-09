describe("Sending a request", () => {
    it("Send a request successfully", () => {
        cy.visit("/listing/:id")

        cy.get("[data-cy=message]").type("Ønsker å leie denne!")
        cy.get("[data-cy=startDate]").type("2022-05-05")
        cy.get("[data-cy=endDate]").type("2022-06-05")

        cy.intercept(
            {
                method: "POST",
                url: "http://localhost:8888/request",
            },
            (req) => {
                expect(req.body.message).to.include("Ønsker å leie denne!")
                expect(req.body.startDate).to.include("2022-05-05")
                expect(req.body.endDate).to.include("2022-06-05")
                req.reply({
                    status: "Success",
                })
            }
        ).as("postRequest")
        cy.get("[data-cy=sendRequest").click()
        cy.wait("@postRequest")
    })

    it("Send request unsuccessfully", () => {
        cy.visit("/listing/:id")

        cy.get("[data-cy=message]").type("Ønsker å leie denne!")
        cy.get("[data-cy=startDate]").type("2022-08-05")
        cy.get("[data-cy=endDate]").type("2022-06-05")

        cy.contains("Sluttdato må være etter startdato")
        cy.get("[data-cy=sendRequest]").click()
    })
})
