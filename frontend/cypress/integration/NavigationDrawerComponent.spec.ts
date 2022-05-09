describe("Verify NavBar Functionality", () => {
    it("Test Update Navbar At Login", () => {
        cy.visit("/")

        cy.get("[data-cy=navbar-login-button]").click()
        cy.get("[data-cy=username]").type("john@doe.org")
        cy.get("[data-cy=password]").type("password")
        cy.intercept(
            {
                method: "POST",
                url: "http://localhost:8888/auth*",
            },
            (req) => {
                expect(req.body.username).to.include("john@doe.org")
                expect(req.body.password).to.include("password")

                req.reply({
                    token: "testtest",
                })
            }
        ).as("getToken")
        cy.intercept(
            {
                method: "GET",
                url: "http://localhost:8888/user",
            },
            (req) => {
                expect(req.headers.authorization).to.equal("Bearer testtest")

                req.reply({
                    email: "john@doe.org",
                    firstname: "John",
                    lastname: "Doe",
                    phone: "+4700000000",
                    role: "USER",
                    zip: "0",
                })
            }
        ).as("getUserInfo")

        //Verify that login-button exists before logon
        cy.get("[data-cy=navdrawer-login-button]").should("exist")
        cy.get("[data-cy=navdrawer-user-card]").should("not.exist")
        cy.get("[data-cy=navbar-login-button]").should("exist")
        cy.get("[data-cy=navbar-user-avatar]").should("not.exist")

        //Login user, wait for API-Calls
        cy.get("[data-cy=submit]").click()
        cy.wait("@getToken")
        cy.wait("@getUserInfo")

        //Verify that login-button is gone
        cy.get("[data-cy=navdrawer-login-button]").should("not.exist")
        cy.get("[data-cy=navdrawer-user-card]").should("exist")
        cy.get("[data-cy=navbar-login-button]").should("not.exist")
        cy.get("[data-cy=navbar-user-avatar]").should("exist")
        cy.get("[data-cy=navdrawer-user-card]").contains("John Doe")
        cy.get("[data-cy=navdrawer-user-card]").contains("john@doe.org")
        cy.get("[data-cy=navbar-chat-name]").should("not.exist")
    })

    it("Test Login disappears and name of chat appears while in chat", () => {
        cy.login()
        cy.visit("/chat/*")
        cy.get("[data-cy=navbar-chat-name]").should("exist")
        cy.get("[data-cy=navbar-logo]").should("not.exist")
    })
})
