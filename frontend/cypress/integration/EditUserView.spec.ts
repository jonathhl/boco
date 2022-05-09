describe("Editing a user", () => {
    beforeEach(() => {
        cy.login()
    })

    it("Edit user successfully", () => {
        cy.visit("/user/edit")

        cy.get("[data-cy=firstname]").type("Geir")
        cy.get("[data-cy=lastname]").type("Hansen")
        cy.get("[data-cy=email]").clear().type("geir.hansen@gmail.no")
        cy.get("[data-cy=phonenumber]").clear().type("99995555")
        cy.get("[data-cy=zipcode]").type("8800")
        cy.get("[data-cy=password]").type("Salami")
        cy.get("[data-cy=confirmPassword]").type("Salami")
        cy.intercept(
            {
                method: "PUT",
                url: "http://localhost:8888/user*",
            },
            (req) => {
                expect(req.body.email).to.include("geir.hansen@gmail.no")
                expect(req.body.firstname).to.include("Geir")
                expect(req.body.lastname).to.include("Hansen")
                expect(req.body.password).to.include("Salami")
                expect(req.body.phone).to.include("99995555")
                expect(req.body.zip).to.include("8800")

                req.reply({
                    status: "Success",
                })
            }
        ).as("putUser")
        cy.get("[data-cy=editUser]").click()
        cy.wait("@putUser")
    })

    it("Edit user unsuccessfully", () => {
        cy.visit("/user/edit")
        cy.get("[data-cy=firstname]").type("Geir")
        cy.get("[data-cy=lastname]").type("Hansen")
        cy.get("[data-cy=email]").clear().type("geir.hansen@gmail")
        cy.get("[data-cy=phonenumber]").clear().type("99995555")
        cy.get("[data-cy=zipcode]").type("8800")
        cy.get("[data-cy=password]").type("Salami")
        cy.get("[data-cy=confirmPassword]").type("Salami")
        cy.contains(/Fyll inn en gyldig mailadresse/i)
        cy.get("[data-cy=editUser]").click()
    })
})
