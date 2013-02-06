[languages]
# Not translated

language.en
    en -> English
language.fr
    en -> Français
language.de
    en -> Deutsch
    
[general]

general.yes
	en -> Yes
	fr -> Oui
	de -> Ja

general.no
	en -> No
	fr -> Non
	de -> Nein

general.error.technical
	en -> Technical error
	fr -> Erreur technique
	
general.error.contact
	en -> Please report the following message and identifier to the iTeach administrator.
	fr -> Veuillez signaler le message et l'identifiant qui suivent à l'administrateur d'iTeach.

general.error.full
	en -> ...
		An error has occurred.\n
		@[general.error.contact]\n
		\n
		{0}\n
		Identifier: {1}
		...
	fr -> ...
		Une erreur est survenue.\n
		@[general.error.contact]\n
		{0}\n
		Identifiant : {1}
		...

general.cancel
	en -> Cancel
	fr -> Annuler
	
general.close
	en -> Close
	fr -> Fermer

general.create
	en -> Create
	fr -> Créer

general.submit
	en -> Submit
	fr -> Confirmer

general.update
	en -> Update
	fr -> Modifier

general.delete
	en -> Delete
	fr -> Supprimer

general.loading
	en -> Loading...
	fr -> En cours de chargement...

general.warning
	en -> Warning!
	fr -> Attention !

general.confirm.title
    en -> Confirmation
    fr -> Confirmation
general.error.title
    en -> Error
    fr -> Erreur
    
general.ajax.error
	en -> Communication problem with the server
	fr -> Erreur de communication avec le serveur

[help]

help
	en -> Help
	fr -> Aide
    
[application]

app.login
    en,fr -> @[login]
app.logout
    en -> Log out
    fr -> Déconnexion

# Error page
[error]

page.error
	en -> Error
	fr -> Erreur
	
error.message
	en -> ...
		An error has occurred. We are sorry for any inconvenience.
		...
	fr -> ...
		Une erreur s'est produite. Nous nous excusons pour le problème rencontré.
		...

error.back
	en -> Go back to the portal
	fr -> Revenir à l'acceuil

error.contact.button
    en -> Contact the administrator
    fr -> Contacter l'administrator

error.contact.help
    en -> ...
        The error information has already been sent to the administrator, but you
        can send an additional message if you wish to do so. Just fill and send the form
        below.
        ...
    fr -> ...
        Les informations relatives à l'erreur ont déjà été envoyées à l'administrateur
        mais vous pouvez, si vous le souhaitez, lui envoyer un message supplémentaire,
        en remplissant et en envoyant le formulaire ci-dessous.
        ...

error.contact.send
    en -> Send
    fr -> Envoyer
    de -> Senden

error.contact.placeholder
    en -> Maximum 500 characters
    fr -> Maximum 500 caractères

error.contact.success
    en -> Your message has been sent to the administrator.
    fr -> Votre message a été envoyé à l'administrateur.

error.contact.failed
    en -> Sorry, your message could not be sent.
    fr -> Désolé, votre message n'a pas pu être envoyé.

[login]

login
	en -> Sign in
	fr -> Connexion
	
signInWith
	en -> Sign in with
	fr -> Se connecter avec
	
signInWithUsernameAndPassword
	en -> Sign in with user name and password
	fr -> Se connecter avec un nom d'utilisateur et un mot de passe

page.login
	en,fr -> @[login]

login.user
	en,fr -> @[user.email]

login.password
	en -> Password
	fr -> Mot de passe

login.error
	en -> Incorrect user name or password, or your account has been disabled or has not been confirmed yet.
	fr -> Utilisateur ou mot de passe incorrect, ou votre compte a été désactivé ou n'a pas encore été confirmé.
	
login.openid.error
	en -> Your account could not be validated.
	fr -> Votre compte n'a pas pu être validé.
	
login.registrationOK
	en -> Registration is now OK - you can now log in
	fr -> L'enregistrement s'est bien passé - vous pouvez maintenant vous connecter
    
login.registrationConfirmationNeeded
	en -> Registration is OK and a mail has been sent to you. Follow its instructions before logging in.
	fr -> L'enregistrement s'est bien passé et un courriel vous a été envoyé. Suivez les instructions qu'il contient avant de vous connecter.
		
login.registrationConfirmed
	en -> Your registration has been confirmed. You can now login.
	fr -> Votre enregistrement a été validé. Vous pouvez maintenant vous connecter.
		
login.registrationConfirmationFailed
	en -> The confirmation of your registration has failed. Contact the administrator.
	fr -> La confirmation de votre enregistrement a échoué. Contactez l'administrateur.

[registration]

page.registrationForm
    en -> Registration
    fr -> Inscription
    
register
	en,fr -> @[registration.submit]

registration.password
    en -> Password
    fr -> Mot de passe
registration.password.confirm
    en -> Confirm
    fr -> Confirmation
registration.password.confirm.help
	en -> Enter the same password again
	fr -> Entrez de nouveau votre mot de passe pour le confirmer
registration.submit
    en -> Register
    fr -> S'enregistrer
    
registration.email.help
	en -> This email will be used as your identifier when you want to connect.
	fr -> Ce courriel sera utilisé comme identifiant pour votre connexion.

registration.admin.email.help
	en -> ...
		However, it won't be controlled so make sure
		it is valid before going on.
		...
	fr -> ...
		Cependant, il ne sera pas controllé alors soyez
		sûr de sa validité avant de continuer.
		...
	
registration.admin.message
	en -> ...
		No administrator has not been defined yet for the application
		and one must be created.
		...
	fr -> ...
		Aucun administrateur n'a encore été défini pour l'application
		et un doit être créé maintenant.
		...

[home]

page.index
    en -> Home page
    fr -> Page d'accueil
    
page.index.message
	en -> ...
		iTeach is the application that will help you organise your courses and your
		planning.
		...
	fr -> ...
		iTeach est l'application pour vous aider à organiser vos leçons et
		votre agenda.
		...
    
page.index.enter
    en -> Enter @[app.title]
    fr -> Entrer dans @[app.title]

page.home
    en -> Planning
    fr -> Planning

home.schools
	en -> My schools
	fr -> Mes écoles

home.planning
	en -> My planning
	fr -> Mon planning
	
home.students
	en -> My students
	fr -> Mes étudiants

[schools]

school.new
	en -> New school
	fr -> Nouvelle école

school.new.tip
	en -> ...
		Create a school by giving it a name and optionally a colour. You can also
		associate coordinates to it. Any data can be updated later on.
		...
	fr -> ...
		Créez une école en lui donnant un nom et une couleur (optionel). Vous pouvez
		également lui attribuer des coordonnées de contact. Toute donnée peut être
		modifiée ultérieurement.
		...

school.new.error
	en -> Error while creating a school
	fr -> Erreur lors de la création de l'école

school.delete
	en -> Delete school "{0}"
	fr -> Supprimer l'école "{0}"
	
school.delete.prompt
	en -> Do you really want to delete the "{0}" school?\nAll associated data (students, lessons) will deleted as well.
	fr -> Voulez-vous vraiment supprimer l'école "{0}" ?\nToutes les données associées (étudiants, leçons) seront également supprimées.
	
school.delete.error
	en -> Error while deleting the school
	fr -> Erreur lors de la suppression de l'école
	
school.edit
	en -> Edit school "{0}"
	fr -> Editer l'école "{0}"
	
school.edit.error
	en -> Error while updating the school
	fr -> Erreur lors de la modification de l'école

school.name
	en -> School name
	fr -> Nom de l'école

school.color
	en -> Colour code
	fr -> Code couleur

school.hourlyRate
	en -> Hourly rate
	fr -> Taux horaire
	
school.hourlyRate.wrong
	en -> The hourly rate must be a positive number
	fr -> Le taux horaire doit être un nombre positif

school.coordinates
    en -> Coordinates
    fr -> Coordonnées

school.details.data
	en -> Its data
	fr -> Ses données

school.details.coordinates
	en -> Its coordinates
	fr -> Ses coordonnées

school.details.students
	en -> Its students
	fr -> Ses étudiants
	
school.details.totalHours
	en -> Total hours
	fr -> Total d'heures

school.details.comments
	en -> Your comments
	fr -> Vos commentaires

school.student.hours
	en -> {0}h
	fr -> {0}h
	de -> {0}h

[students]

student.new
	en -> New student
	fr -> Nouvel étudiant

student.new.noschool
	en -> You have to create a school before to create a student.
	fr -> Vous devez créer une école avant de créer un étudiant.

student.new.tip
	en -> ...
		Create a student by giving it a name and a lesson subject. You can also
		associate coordinates to it. Any data can be updated later on.
		...
	fr -> ...
		Créez un étudiant en lui donnant un nom et un sujet de leçon. Vous pouvez
		également lui attribuer des coordonnées de contact. Toute donnée peut être
		modifiée ultérieurement.
		...

student.new.error
	en -> Error while creating the student
	fr -> Erreur lors de la création de l'étudiant

student.delete
	en -> Delete student "{0} {1}"
	fr -> Supprimer l'étudiant "{0} {1}"
	
student.delete.prompt
	en -> Do you really want to delete the "{0}" student?\nAll associated lessons will deleted as well.
	fr -> Voulez-vous vraiment supprimer l'étudiant "{0}" ?\nToutes les leçons associées seront également supprimées.
	
student.delete.error
	en -> Error while deleting the student
	fr -> Erreur lors de la suppression de l'étudiant
	
student.edit
	en -> Edit student "{0}"
	fr -> Editer l'étudiant "{0}"
	
student.edit.error
	en -> Error while updating the student
	fr -> Erreur lors de la modification de l'étudiant

student.link
	en -> Displays details for the student
	fr -> Affiche les détails pour cet étudiant	

student.name
	en -> Name
	fr -> Nom

student.subject
	en -> Subject
	fr -> Matière

student.school
	en -> Student's school
	fr -> Ecole de l'étudiant
	
student.school.hourlyRate
	en -> School hourly rate
	fr -> Taux horaire de l'école

student.coordinates
    en -> Coordinates
    fr -> Coordonnées
	
student.details.data
	en -> His data
	fr -> Ses données

student.details.coordinates
	en -> His coordinates
	fr -> Ses coordonnées

student.details.lessons
	en -> His lessons
	fr -> Ses leçons

student.lessons.hours.total
	en -> Total hours
	fr -> Total d'heures

student.lessons.hours.month
	en -> Hours for the month
	fr -> Heures pour le mois

student.details.lessons.error
	en -> Cannot load lessons
	fr -> Impossible de charger les leçons

student.details.comments
	en -> Your comments
	fr -> Vos commentaires

[lessons]

lesson.new
	en -> New lesson
	fr -> Nouvelle leçon

lesson.tip
	en -> ...
		Create a new lesson by selection a date, a period of time, a student
		and an optional location.
		...
	fr -> ...
		Créez une nouvelle leçon en fournissant une date, une période, un étudiant
		et éventuellement un emplacement.
		...

lesson.date
	en -> Date
	fr -> Date

lesson.from
	en -> From
	fr -> De

lesson.to
	en -> to
	fr -> à

lesson.student
	en -> Student
	fr -> Etudiant

lesson.location
	en -> Location
	fr -> Emplacement
	
lesson.error.timeformat
	en -> Wrong time format: {0}
	fr -> Format incorrect: {0}

lesson.error.timeorder
	en -> The end time must be later than the start time
	fr -> L'heure de fin doit être plus tard que l'heure de début

lesson.new.error
	en -> Error while creating the lesson
	fr -> Erreur lors de la création de la leçon

lesson.fetch.error
	en -> Error while fecthing the lessons for this period
	fr -> Erreur lors de la requête pour les leçons de cette période

lesson.edit
	en -> Edit lesson
	fr -> Modifier la leçon

lesson.edit.error
	en -> Error while modifying the lesson
	fr -> Erreur lors de la modification de la leçon

lesson.delete.prompt
	en -> Do you really want to delete this lesson?
	fr -> Voulez-vous vraiment supprimer cette leçon ?

lesson.delete.error
	en -> Error while deleting this lesson
	fr -> Erreur lors de la suppression de cette leçon
	
lesson.change.error
	en -> Error while changing the lesson schedule
	fr -> Erreur pendant la modification de la leçon

lesson.details.data
	en -> Schedule
	fr -> Planning

lesson.details.student
	en -> The student
	fr -> L'étudiant

lesson.details.school
	en -> The school
	fr -> L'école

lesson.details.comments
	en -> Your comments
	fr -> Vos commentaires
	
[coordinates]

coordinates.get.error
	en -> Error while getting the coordinates
	fr -> Erreur lors de l'accès aux coordonnées

[comments]

comments.loading.error
	en -> Error while loading the comments
	fr -> Erreur lors du chargement des commentaires

comment.loading.more
	en -> More...
	fr -> Suite...

comment.loading.more.tip
	en -> Load the next comments.
	fr -> Charge les commentaires suivants.

comment.new
	en -> Add a comment
	fr -> Ajouter un commentaire

comment.new.error
	en -> Error while creating the comment
	fr -> Erreur lors de la création du commentaire

comment.content
	en -> Content
	fr -> Contenu

comment.preview
	en -> Preview
	fr -> Prévisualisation

comments.preview.error
	en -> Cannot get preview
	fr -> Impossible d'avoir la prévisualisation

comment.delete.prompt
	en -> Do you want to delete this comment?
	fr -> Voulez-vous supprimer ce commentaire?

comment.delete.error
	en -> Error while deleting the comment
	fr -> Erreur lors de la suppression du commentaire

comment.edit
	en -> Edit comment
	fr -> Editer le commentaire

comment.edit.error
	en -> Error while updating the comment
	fr -> Erreur lors de la mofication du commentaire

comment.edition
	en -> edited on {0}
	fr -> édité le {0}

comment.more
	en -> more
	fr -> suite

comment.more.tip
	en -> Loads the full content.
	fr -> Charge le contenu complet.
	
comment.content.help
	en -> ...
		<ul>
			<li>Links are rendered as links</li>
			<li><code>*...*</code> is rendered as <b>bold</b></li>
			<li><code>_..._</code> is rendered as <i>italic</i></li>
		</ul>
		...
	fr -> ...
		<ul>
			<li>Les liens sont rendus en tant que liens</li>
			<li><code>*...*</code> est affiché en <b>gras</b></li>
			<li><code>_..._</code> est affiché en <i>italique</i></li>
		</ul>
		...

[admin]

admin.accounts.link
	en -> @[page.accounts]
	fr -> @[page.accounts]

page.accounts
	en -> Manage accounts
	fr -> Gestion des comptes

admin.accounts.mode
	en -> Authentication mode
	fr -> Mode d'authentification
admin.accounts.verified
	en -> Verified
	fr -> Vérifié

admin.accounts.mode.openid
	en,fr,de -> OpenID
admin.accounts.mode.password
	en -> Password
	fr -> Mot de passe

page.accountDelete
	en -> Account deletion confirmation
	fr -> Confirmation pour la suppression d'un compte

page.accountDelete.prompt
	en -> Are you sure to delete the following account? All his data will be lost! No cancelletion will be possible.
	fr -> Etes-vous sûr de supprimer le compte suivant ? Toutes ses données seront perdues ! Aucune annulation n'est possible.
	
admin.settings
	en -> Settings
	fr -> Configuration

admin.settings.ok
	en -> Settings have been saved.
	fr -> La configuration a été enregistrée.
	
page.settings
	en,fr -> @[admin.settings]

[profile]

page.profile
	en -> Account
	fr -> Compte
	
profile.data
	en -> General data
	fr -> Données générales
	
profile.stats
	en -> Statistics
	fr -> Statistiques
	
profile.administrator
	en -> Administrator
	fr -> Administrateur

profile.schoolCount
	en -> Schools
	fr -> Ecoles

profile.studentCount
	en -> Students
	fr -> Etudiants

profile.lessonCount
	en -> Lessons
	fr -> Leçons

profile.actions
	en -> Actions
	fr -> Actions

profile.changePassword
	en -> Change password
	fr -> Changer de mot de passe
	
page.passwordChange
	en,fr -> @[profile.changePassword]

page.passwordRequest
	en -> Password change request
	fr -> Demande de changement de mot de passe

page.passwordRequest.message
	en -> ...
		Your request for the change of your password has been accepted. An email has been sent to you. Follow its instructions
		to complete the change.
		...
	fr -> ...
		Votre demande de changement de mot de passe a été acceptée. Un courriel vous a été envoyé. Suivez les instructions qu'il
		contient afin de compléter le changement.
		...

page.passwordRequest.back
	en -> Go back to the planning
	fr -> Retourner au planning

page.passwordRequest.oldPassword
	en -> Old password
	fr -> Ancien mot de passe
	
page.passwordRequest.newPassword
	en -> New password
	fr -> Nouveau mot de passe
	
page.passwordRequest.confirmPassword
	en -> Confirm password
	fr -> Confirmation

page.passwordRequest.submit
	en -> Change
	fr -> Changer

page.passwordRequest.ok
	en -> Your password has been successfully changed.
	fr -> Votre mot de passe a été changé.

page.passwordRequest.error
	en -> Your password could not be changed. Maybe your old password was incorrect.
	fr -> Votre mot de passe n'a pas pu être changé. Votre ancien mot de passe était peut-être incorrect.

[reports]

report.monthly
	en -> Monthly report
	fr -> Rapport mensuel
	
report.monthly.title
	en,fr -> @[report.monthly] ({0})

report.monthly.tip
	en -> Displays a report of all students hours, grouped per school.
	fr -> Affiche un rapport pour toutes les heures des étudiants, groupés par école.

report.school
	en -> School
	fr -> Ecole

report.student
	en -> Student
	fr -> Etudiant

report.hours
	en -> Hours
	fr -> Heures

report.total
	en -> Total
	fr -> Total

report.amount
	en -> Amount
	fr -> Montant

report.amount.na
	en -> Several currencies
	fr -> Plusieurs devises
