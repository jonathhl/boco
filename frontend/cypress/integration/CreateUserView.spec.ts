describe("Creating a new user", () => {
    it("Create new user successfully", () => {
        cy.visit("/user/create")

        cy.get("[data-cy=firstname]").type("Ola")
        cy.get("[data-cy=lastname]").type("Nordmann")
        cy.get("[data-cy=email]").type("ola.nordmann@gmail.no")
        cy.get("[data-cy=phonenumber]").type("99994444")
        cy.get("[data-cy=zipcode]").type("7030")
        cy.get("[data-cy=city]").type("Trondheim")
        cy.get("[data-cy=password]").type("Passord")
        cy.get("[data-cy=confirmPassword]").type("Passord")
        cy.intercept(
            {
                method: "POST",
                url: "http://localhost:8888/user*",
            },
            (req) => {
                expect(req.body.email).to.include("ola.nordmann@gmail.no")
                expect(req.body.firstname).to.include("Ola")
                expect(req.body.lastname).to.include("Nordmann")
                expect(req.body.password).to.include("Passord")
                expect(req.body.phone).to.include("99994444")
                expect(req.body.zip).to.include("7030")

                req.reply({
                    status: "Success",
                })
            }
        ).as("postUser")
        cy.get("[data-cy=createUser]").click()
        cy.wait("@postUser")
    })

    it("Create new user unsuccessfully", () => {
        cy.visit("/user/create")

        cy.get("[data-cy=firstname]").type("Ola")
        cy.get("[data-cy=lastname]").type("Nordmann")
        cy.get("[data-cy=email]").type("ola.nordmann@gmail")
        cy.get("[data-cy=phonenumber]").type("99994444")
        cy.get("[data-cy=zipcode]").type("7030")
        cy.get("[data-cy=city]").type("Trondheim")
        cy.get("[data-cy=password]").type("Passord")
        cy.get("[data-cy=confirmPassword]").type("Passord")
        cy.contains(/Fyll inn en gyldig mailadresse/i)
        cy.get("[data-cy=createUser]").click()
    })
})
