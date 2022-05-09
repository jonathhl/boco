describe("Logging in a user", () => {
    it("Logging in user successfully", () => {
        cy.visit("/*")

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
                    //token: "testtest",
                    email: "john@doe.org",
                    firstname: "John",
                    lastname: "Doe",
                    phone: "+4700000000",
                    role: "USER",
                    zip: "0",
                })
            }
        ).as("getUserInfo")
        cy.get("[data-cy=submit]").click()
        cy.wait("@getToken")
        cy.wait("@getUserInfo")
    })

    it("Brukernavnet må være en e-post", () => {
        cy.visit("/*")

        cy.get("[data-cy=navbar-login-button]").click()
        cy.get("[data-cy=username]").type("ola.nordmanngmail")
        cy.get("[data-cy=password]").type("Passord")
        cy.contains(/Brukernavnet må være en e-post/i) //Fyll inn en gyldig mailadresse
        cy.get("[data-cy=submit]").click()
    })

    it("Logging in user unsuccessfully, wrong password", () => {
        cy.visit("/*")

        cy.get("[data-cy=navbar-login-button]").click()
        cy.get("[data-cy=username]").type("john@doe.org")
        cy.get("[data-cy=password]").type("password")
        cy.intercept(
            {
                method: "POST",
                url: "http://localhost:8888/auth",
            },
            (req) => {
                expect(req.body.username).to.include("john@doe.org")
                expect(req.body.password).not.to.include("hei")

                req.reply({
                    status: "403",
                })
            }
        ).as("getToken")
        cy.get("[data-cy=submit]").click()
        cy.contains(/Feil brukernavn eller passord/i) //Fyll inn en gyldig mailadresse
        cy.wait("@getToken")
    })

    it("Should redirect to Create User when pressing register button", function () {
        cy.visit("/*")
        cy.get("[data-cy=navbar-login-button]").click()
        cy.get("[data-cy=register]").click()
        cy.contains(/Registrer deg/i)
    })
})
